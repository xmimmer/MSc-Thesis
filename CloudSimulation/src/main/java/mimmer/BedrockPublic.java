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
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple example showing how to create 2 datacenters with 1 host each and run
 * cloudlets of 2 users. It also sets a network topology.
 */
public class BedrockPublic {
    private static final int VM_PES = 1;

    //private final List<Datacenter> datacenterList;
    private final Datacenter dc1;
    private final Datacenter dc2;
    //private final List<DatacenterBroker> brokerList;
    private final DatacenterBroker broker1;
    private final DatacenterBroker broker2;

    private final List<Cloudlet> cloudletList1;
    private final List<Cloudlet> cloudletList2;
    private final List<Vm> vmList1;
    private final List<Vm> vmList2;
    private final CloudSim simulation;

    public static void main(String[] args) {
        new BedrockPublic();
    }

    private BedrockPublic() {

        System.out.println("Starting " + getClass().getSimpleName());

        vmList1 = new ArrayList<>();
        vmList2 = new ArrayList<>();

        simulation = new CloudSim();

        dc1 = createDatacenter();
        dc2 = createDatacenter();

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
        final long length = 40000;
        final long fileSize = 300;
        final long outputSize = 300;
        final UtilizationModel utilizationModel = new UtilizationModelFull();

        final Cloudlet cloudlet1 =
                new CloudletSimple(length, VM_PES)
                        .setFileSize(fileSize)
                        .setOutputSize(outputSize)
                        .setUtilizationModel(utilizationModel);

        final Cloudlet cloudlet2 =
                new CloudletSimple(length, VM_PES)
                        .setFileSize(fileSize)
                        .setOutputSize(outputSize)
                        .setUtilizationModel(utilizationModel);

        cloudletList1.add(cloudlet1);
        cloudletList2.add(cloudlet2);

        broker1.submitCloudletList(cloudletList1);
        broker2.submitCloudletList(cloudletList2);
    }

    private void createAndSubmitVms() {
        final long size = 10000; //image size (Megabyte)
        final int mips = 250;
        final int ram = 512; //vm memory (Megabyte)
        final long bw = 1000;

        final Vm vm1 = new VmSimple(mips, VM_PES)
                .setRam(ram).setBw(bw).setSize(size);

        final Vm vm2 = new VmSimple(mips, VM_PES)
                .setRam(ram).setBw(bw).setSize(size);

        vmList1.add(vm1);
        vmList2.add(vm2);

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

    private Datacenter createDatacenter() {
        final List<Host> hostList = new ArrayList<>();
        final List<Pe> peList = new ArrayList<>();

        final long mips = 1000;
        peList.add(new PeSimple(mips, new PeProvisionerSimple()));

        final long ram = 2048; // in Megabytes
        final long storage = 1000000; // in Megabytes
        final long bw = 10000; //in Megabits/s

        final Host host = new HostSimple(ram, bw, storage, peList);
        hostList.add(host);

        return new DatacenterSimple(simulation, hostList);
    }
}
