/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */
package mimmer;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.power.models.PowerModelHostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple example showing how to create 2 datacenters with 1 host each and run
 * cloudlets of 2 users. It also sets a network topology.
 */
public class BedrockPublic {

    //Datacenters
    private Datacenter dc1;
    private Datacenter dc2;
    private static final int VM_PES = 1;
    private DatacenterBroker broker1;
    private DatacenterBroker broker2;

    private final List<Cloudlet> cloudletList1;
    private final List<Cloudlet> cloudletList2;
    private final List<Vm> vmList1;
    private final List<Vm> vmList2;
    private final CloudSim simulation;
    private final List<Host> hostList;

    public static void main(String[] args) {
        new BedrockPublic();
    }

    private BedrockPublic() {

        System.out.println("Starting " + getClass().getSimpleName());

        vmList1 = new ArrayList<>();
        vmList2 = new ArrayList<>();
        hostList = new ArrayList<>(10);

        simulation = new CloudSim();

        createDatacenter1();
        createDatacenter2();

        broker1 = new DatacenterBrokerSimple(simulation);
        broker2 = new DatacenterBrokerSimple(simulation);


        createNetwork();
        createAndSubmitVms();

        cloudletList1 = new ArrayList<>();
        cloudletList2 = new ArrayList<>();
        createAndSubmitCloudlets();

        simulation.start();

        printFinishedCloudletList(broker1);
        printFinishedCloudletList(broker2);

        System.out.println(getClass().getSimpleName() + " finished!");


    }

    private void printFinishedCloudletList(DatacenterBroker broker) {
        new CloudletsTableBuilder(broker.getCloudletFinishedList())
                .setTitle("Broker " + broker)
                .build();
    }

    private void createAndSubmitCloudlets() {
        final long length = 10000;
        final long fileSize = 300;
        final long outputSize = 300;

        final UtilizationModel utilizationModel = new UtilizationModelFull();

        for(int i = 0; i <= 5; i++) {
            final Cloudlet cloudlet = new CloudletSimple(length, VM_PES)
                    .setSizes(fileSize)
                    .setUtilizationModel(utilizationModel);
            cloudletList1.add(cloudlet);
        }
        for(int i = 0; i <= 5; i++) {
            final Cloudlet cloudlet = new CloudletSimple(length, VM_PES)
                    .setSizes(fileSize)
                    .setUtilizationModel(utilizationModel);
            cloudletList2.add(cloudlet);
        }
        broker1.submitCloudletList(cloudletList1);
        broker2.submitCloudletList(cloudletList2);

    }

    private void createAndSubmitVms() {
        final long size = 10000; //image size (Megabyte)
        final int mips = 1000;
        final int ram = 512; //vm memory (Megabyte)
        final long bw = 1000;

        for(int i = 0; i<=5; i++) {
            final Vm vm = new VmSimple(mips, VM_PES)
                    .setRam(ram).setBw(bw).setSize(size);
            vmList1.add(vm);
        }
        for(int i = 0; i<=5; i++) {
            final Vm vm = new VmSimple(mips, VM_PES)
                    .setRam(ram).setBw(bw).setSize(size);
            vmList2.add(vm);
        }

        broker1.submitVmList(vmList1);
        broker2.submitVmList(vmList2);
    }

    /**
     * Creates the network topology from a brite file.
     */
    private void createNetwork() {
        //load the network topology file
        final var networkTopology = BriteNetworkTopology.getInstance("topology.brite");
        simulation.setNetworkTopology(networkTopology);

        //Maps CloudSim entities to BRITE entities
        //Datacenter0 will correspond to BRITE node 0
        int briteNode = 0;
        networkTopology.mapNode(dc1, briteNode);

        //Datacenter1 will correspond to BRITE node 2
        briteNode = 2;
        networkTopology.mapNode(dc2, briteNode);

        //Broker1 will correspond to BRITE node 3
        briteNode = 3;
        networkTopology.mapNode(broker1, briteNode);

        //Broker2 will correspond to BRITE node 4
        briteNode = 4;
        networkTopology.mapNode(broker2, briteNode);
    }

    private Datacenter createDatacenter1() {
        for (int i = 0; i < 5; i++) {
            hostList.add(createHost(i));
        }

        final var dc1 = new DatacenterSimple(simulation, hostList);
        dc1.setName("Datacenter1");

        return dc1;
    }
    private Datacenter createDatacenter2() {

        for (int i = 0; i < 5; i++) {
            hostList.add(createHost(i));
        }

        final var dc1 = new DatacenterSimple(simulation, hostList);
        dc1.setName("Datacenter2");

        return dc1;
    }
    private Host createHost(int id) {
        final List<Pe> peList = new ArrayList<>(64);

        for (int i = 0; i < 64; i++) {
            peList.add(new PeSimple(1000, new PeProvisionerSimple()));
        }

        final var host = new HostSimple(1000000, 10000000, 1000000, peList);


        host.setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerTimeShared());

        host.setId(id);
        host.enableUtilizationStats();

        return host;
    }
}
