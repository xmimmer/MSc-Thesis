package mimmer;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.power.models.PowerModelHost;
import org.cloudbus.cloudsim.power.models.PowerModelHostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.*;
import org.cloudsimplus.autoscaling.HorizontalVmScaling;
import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.listeners.EventInfo;
import org.cloudsimplus.listeners.EventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;

public class BedrockPrivateLoadBalancer {

    //Conversion
    int GB = 1000;

    //Datacenter(s)
    private int datacenters = 1; // Amount of data centers
    private static final int scheduling_interval = 5; // Sets the scheduling delay to process each event received by the Datacenter (Seconds)
    private static final int cloudlets_creation_interval = scheduling_interval * 2; // Interval between creation of cloudlets


    //Host properties
    private int hosts = 10; // 10 Hosts representing physical servers
    private int host_mips = 1000; // Million instructions per second (MIPS) of each host
    private int host_pes = 32; // 32 CPU Cores per host
    private int host_ram = 64*GB; // 64GB RAM
    private int host_bw = 10000000; // 10Gbps
    private int host_storage = 256000; // 256 GB
    private int host_startup_delay = 5; // Seconds
    private int host_shutdown_delay = 3; // Seconds
    private int host_startup_power = 5; // Startup power in Watts
    private int host_shutdown_power = 3; // Shutdown power in Watts
    private int static_power = 35; // Idle power in Watts
    private int max_power = 100; // Max power in Watts

    // Virtual Machines
    private int VMs = hosts*3; // Amount of virtual machines in total
    private int VM_ram = 8*GB; // RAM of each virtual machine (MB)
    private int VM_pes = 8; // CPU cores per virtual machine
    private int VM_bw = 500000; // Bandwidth capacity per virtual machine (Mbps)
    private int VM_storage = 50000; // Storage capacity per virtual machine (MB)
    private int createdVms;

    // Cloudlets (User-defined workload)
    private int cloudlets = 0; // Initial amount of cloudlets representing workload (Can be set to 0 for dynamic cloudlets only)
    private int cloudlet_pes = 2; // Number of required PE's to run each cloudlet
    private static final long[] CLOUDLET_LENGTHS = {10000, 20000, 30000, 40000, 50000}; // Range of possible cloudlet sizes
    private int createdCloudlets; // Variable tracking number of created cloudlets

    // Declaration of other simulation components
    private final CloudSim simulation;
    private final Datacenter datacenter;
    private final DatacenterBroker broker;
    private final List<Host> hostList;
    private final List<Vm> vmList;
    private final List<Cloudlet> cloudletList;
    private final ContinuousDistribution rand;

    public static void main(String[] args) {
        new BedrockPrivateLoadBalancer();
    }
    private BedrockPrivateLoadBalancer() {

        final long seed = 1;
        rand = new UniformDistr(0, CLOUDLET_LENGTHS.length, seed);
        hostList = new ArrayList<>(hosts);
        vmList = new ArrayList<>(VMs);
        cloudletList = new ArrayList<>(cloudlets);
        createCloudletList();

        simulation = new CloudSim();
        simulation.addOnClockTickListener(this::createNewCloudlets);

        datacenter = createDatacenter();

        broker = new DatacenterBrokerSimple(simulation);
        broker.setVmDestructionDelay(60.0);

        vmList.addAll(createListOfScalableVms(VMs));

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        simulation.start();

        printSimulationResults();
        printTotalVmsCost();
        printHostsCpuUtilizationAndPowerConsumption();
        printVmsCpuUtilizationAndPowerConsumption();
        printHostsUpTime();

    }

    private Datacenter createDatacenter() {
        for (int i = 0; i < hosts; i++) {
            hostList.add(createHost(i));
        }

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        final Datacenter dc = new DatacenterSimple(simulation, hostList).setSchedulingInterval(scheduling_interval);

        // Those are monetary values. Consider any currency you want (such as Dollar)
        dc.getCharacteristics()
                .setCostPerSecond(0.002)
                .setCostPerMem(0.0001)
                .setCostPerStorage(0.00002)
                .setCostPerBw(0.000001);
        return dc;
    }

    private Host createHost(int id) {
        final List<Pe> peList = new ArrayList<>(host_pes);

        for (int i = 0; i < host_pes; i++) {
            peList.add(new PeSimple(host_mips, new PeProvisionerSimple()));
        }

        final var host = new HostSimple(host_ram, host_bw, host_storage, peList);

        final var powerModel = new PowerModelHostSimple(max_power, static_power);
       powerModel.setStartupDelay(host_startup_delay)
                .setShutDownDelay(host_shutdown_delay)
                .setStartupPower(host_startup_power)
                .setShutDownPower(host_shutdown_power);

        host.setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerTimeShared())
                .setPowerModel(powerModel);

        host.setId(id);
        host.enableUtilizationStats();

        return host;
    }

    private Vm createVm() {
        final int id = createdVms++;
        final var vm = new VmSimple(id, host_mips, VM_pes);
        vm.setRam(VM_ram).setBw(VM_bw).setSize(VM_storage);
        vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
        vm.enableUtilizationStats();

        return vm;

    }

    private List<Vm> createListOfScalableVms(final int vmsNumber) {
        final List<Vm> newList = new ArrayList<>(vmsNumber);
        for (int i = 0; i < vmsNumber; i++) {
            final Vm vm = createVm();
            createHorizontalVmScaling(vm);
            vm.enableUtilizationStats();
            newList.add(vm);
        }

        return newList;
    }

    private void createHorizontalVmScaling(final Vm vm) {
        final HorizontalVmScaling horizontalScaling = new HorizontalVmScalingSimple();
        horizontalScaling
                .setVmSupplier(this::createVm)
                .setOverloadPredicate(this::isVmOverloaded);
        vm.setHorizontalScaling(horizontalScaling);
    }

    private boolean isVmOverloaded(final Vm vm) {
        return vm.getCpuPercentUtilization() > 0.7;
    }

    private void printTotalVmsCost() {
        System.out.println();
        double totalCost = 0.0;
        int totalNonIdleVms = 0;
        double processingTotalCost = 0, memoryTotaCost = 0, storageTotalCost = 0, bwTotalCost = 0;
        for (final Vm vm : broker.getVmCreatedList()) {
            final VmCost cost = new VmCost(vm);
            processingTotalCost += cost.getProcessingCost();
            memoryTotaCost += cost.getMemoryCost();
            storageTotalCost += cost.getStorageCost();
            bwTotalCost += cost.getBwCost();

            totalCost += cost.getTotalCost();
            totalNonIdleVms += vm.getTotalExecutionTime() > 0 ? 1 : 0;
            System.out.println(cost);
        }

        System.out.printf(
                "Total cost ($) for %3d created VMs from %3d in DC %d: %8.2f$ %13.2f$ %17.2f$ %12.2f$ %15.2f$%n",
                totalNonIdleVms, broker.getVmsNumber(), datacenter.getId(),
                processingTotalCost, memoryTotaCost, storageTotalCost, bwTotalCost, totalCost);
    }
    private void printHostCpuUtilizationAndPowerConsumption(final Host host) {
        final HostResourceStats cpuStats = host.getCpuUtilizationStats();

        //The total Host's CPU utilization for the time specified by the map key
        final double utilizationPercentMean = cpuStats.getMean();
        final double watts = host.getPowerModel().getPower(utilizationPercentMean);
        System.out.printf(
                "Host %2d CPU Usage mean: %6.1f%% | Power Consumption mean: %8.0f W%n",
                host.getId(), utilizationPercentMean * 100, watts);
    }
    private void printHostsCpuUtilizationAndPowerConsumption() {
        System.out.println();
        for (final Host host : hostList) {
            printHostCpuUtilizationAndPowerConsumption(host);
        }
        System.out.println();
    }
    private void printVmsCpuUtilizationAndPowerConsumption() {
        vmList.sort(comparingLong(vm -> vm.getHost().getId()));
        for (Vm vm : vmList) {
            final var powerModel = vm.getHost().getPowerModel();
            final double hostStaticPower = powerModel instanceof PowerModelHostSimple powerModelHost ? powerModelHost.getStaticPower() : 0;
            final double hostStaticPowerByVm = hostStaticPower / vm.getHost().getVmCreatedList().size();

            //VM CPU utilization relative to the host capacity
            final double vmRelativeCpuUtilization = vm.getCpuUtilizationStats().getMean() / vm.getHost().getVmCreatedList().size();
            final double vmPower = powerModel.getPower(vmRelativeCpuUtilization) - hostStaticPower + hostStaticPowerByVm; // W
            final VmResourceStats cpuStats = vm.getCpuUtilizationStats();
            System.out.printf(
                    "Vm   %2d CPU Usage Mean: %6.1f%% | Power Consumption Mean: %8.0f W%n",
                    vm.getId(), cpuStats.getMean() *100, vmPower);
        }
    }
    private void printHostsUpTime() {
        System.out.printf("%nHosts' up time (total time each Host was powered on)%n");
        datacenter.getHostList().stream().filter(Host::hasEverStarted).forEach(host -> {
            final PowerModelHost powerModel = host.getPowerModel();
            System.out.printf("  Host %2d%n", host.getId());

            System.out.printf(
                    "     Total Up time:  %3.0f secs |  Startup time: %3.0f secs | Startup power:  %3.0f watts%n",
                    host.getTotalUpTime(), powerModel.getTotalStartupTime(), powerModel.getTotalStartupPower());

            System.out.printf(
                    "     Activations:    %3d      | Shutdown time: %3.0f secs | Shutdown power: %3.0f watts%n",
                    powerModel.getTotalStartups(), powerModel.getTotalShutDownTime(), powerModel.getTotalShutDownPower());

        });
    }

    private void createNewCloudlets(final EventInfo info) {
        final long time = (long) info.getTime();
        System.out.println(time);
        if (time % cloudlets_creation_interval == 0 && time < 30) {
            final int cloudletsNumber = 3;
            System.out.printf("\t#Creating %d Cloudlets at time %d.%n", cloudletsNumber, time);
            final List<Cloudlet> newCloudlets = new ArrayList<>(cloudletsNumber);
            for (int i = 0; i < cloudletsNumber; i++) {
                final Cloudlet cloudlet = createCloudlet();
                cloudletList.add(cloudlet);
                newCloudlets.add(cloudlet);
            }

            broker.submitCloudletList(newCloudlets);
        }
    }
    private Cloudlet createCloudlet() {
        final int id = createdCloudlets++;
        final var utilizationModelDynamic = new UtilizationModelDynamic(0.1);

        //Randomly selects a length for the cloudlet
        final long length = CLOUDLET_LENGTHS[(int) rand.sample()];
        return new CloudletSimple(id, length, cloudlet_pes)
                .setFileSize(1024)
                .setOutputSize(1024)
                .setUtilizationModelBw(utilizationModelDynamic)
                .setUtilizationModelRam(utilizationModelDynamic)
                .setUtilizationModelCpu(new UtilizationModelFull());
    }
    private void createCloudletList() {
        for (int i = 0; i < cloudlets; i++) {
            cloudletList.add(createCloudlet());
        }
    }
    private void printSimulationResults() {
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        final Comparator<Cloudlet> sortByVmId = comparingDouble(c -> c.getVm().getId());
        final Comparator<Cloudlet> sortByStartTime = comparingDouble(Cloudlet::getExecStartTime);
        finishedCloudlets.sort(sortByVmId.thenComparing(sortByStartTime));

        new CloudletsTableBuilder(finishedCloudlets).build();

        int cloudletsNumber = finishedCloudlets.size();

        System.out.println("----------------------------");
        System.out.println(cloudletsNumber);
        System.out.println("----------------------------");
    }
}
