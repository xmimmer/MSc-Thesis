/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2021 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package mimmer;
public class CustomClass {

    private static final int HOSTS = 10;
    private static final int HOST_PES = 8; //CPU's
    private static final int  HOST_MIPS = 1000;
    private static final int  HOST_RAM = 2048; //in Megabytes
    private static final long HOST_BW = 10_000; //in Megabits/s
    private static final long HOST_STORAGE = 2_000_000; //in Megabytes
    private static final int VMS = HOSTS*2;
    private static final int VM_PES = HOST_PES/2;
    private static final int CLOUDLETS = VMS*2;
    private static final int CLOUDLET_PES = VM_PES/2;
    private static final int CLOUDLET_LENGTH = HOST_MIPS*10;
    public static void main(String[] args) {

        new CustomClass();
        System.out.println(VMS);
    }

}
