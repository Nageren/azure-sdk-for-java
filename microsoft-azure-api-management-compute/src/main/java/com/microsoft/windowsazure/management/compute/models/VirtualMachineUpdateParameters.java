// 
// Copyright (c) Microsoft and contributors.  All rights reserved.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// 
// See the License for the specific language governing permissions and
// limitations under the License.
// 

// Warning: This code was generated by a tool.
// 
// Changes to this file may cause incorrect behavior and will be lost if the
// code is regenerated.

package com.microsoft.windowsazure.management.compute.models;

import java.util.ArrayList;

/**
* Parameters supplied to the Update Virtual Machine operation.
*/
public class VirtualMachineUpdateParameters
{
    private String availabilitySetName;
    
    /**
    * Optional. Specifies the name of an availability set to which to add the
    * virtual machine. This value controls the virtual machine allocation in
    * the Windows Azure environment. Virtual machines specified in the same
    * availability set are allocated to different nodes to maximize
    * availability.
    */
    public String getAvailabilitySetName() { return this.availabilitySetName; }
    
    /**
    * Optional. Specifies the name of an availability set to which to add the
    * virtual machine. This value controls the virtual machine allocation in
    * the Windows Azure environment. Virtual machines specified in the same
    * availability set are allocated to different nodes to maximize
    * availability.
    */
    public void setAvailabilitySetName(String availabilitySetName) { this.availabilitySetName = availabilitySetName; }
    
    private ArrayList<ConfigurationSet> configurationSets;
    
    /**
    * Contains the collection of configuration sets that contain system and
    * application configuration settings.
    */
    public ArrayList<ConfigurationSet> getConfigurationSets() { return this.configurationSets; }
    
    /**
    * Contains the collection of configuration sets that contain system and
    * application configuration settings.
    */
    public void setConfigurationSets(ArrayList<ConfigurationSet> configurationSets) { this.configurationSets = configurationSets; }
    
    private ArrayList<DataVirtualHardDisk> dataVirtualHardDisks;
    
    /**
    * Contains the parameters Windows Azure used to create the data disk for
    * the virtual machine.
    */
    public ArrayList<DataVirtualHardDisk> getDataVirtualHardDisks() { return this.dataVirtualHardDisks; }
    
    /**
    * Contains the parameters Windows Azure used to create the data disk for
    * the virtual machine.
    */
    public void setDataVirtualHardDisks(ArrayList<DataVirtualHardDisk> dataVirtualHardDisks) { this.dataVirtualHardDisks = dataVirtualHardDisks; }
    
    private String label;
    
    /**
    * Optional. Specifies the friendly name for the virtual machine.
    */
    public String getLabel() { return this.label; }
    
    /**
    * Optional. Specifies the friendly name for the virtual machine.
    */
    public void setLabel(String label) { this.label = label; }
    
    private OSVirtualHardDisk oSVirtualHardDisk;
    
    /**
    * Contains the parameters Windows Azure used to create the operating system
    * disk for the virtual machine.
    */
    public OSVirtualHardDisk getOSVirtualHardDisk() { return this.oSVirtualHardDisk; }
    
    /**
    * Contains the parameters Windows Azure used to create the operating system
    * disk for the virtual machine.
    */
    public void setOSVirtualHardDisk(OSVirtualHardDisk oSVirtualHardDisk) { this.oSVirtualHardDisk = oSVirtualHardDisk; }
    
    private String roleName;
    
    /**
    * Required. Specifies the name for the virtual machine. The name must be
    * unique within the deployment.
    */
    public String getRoleName() { return this.roleName; }
    
    /**
    * Required. Specifies the name for the virtual machine. The name must be
    * unique within the deployment.
    */
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    private VirtualMachineRoleSize roleSize;
    
    /**
    * The size of the virtual machine.
    */
    public VirtualMachineRoleSize getRoleSize() { return this.roleSize; }
    
    /**
    * The size of the virtual machine.
    */
    public void setRoleSize(VirtualMachineRoleSize roleSize) { this.roleSize = roleSize; }
    
    /**
    * Initializes a new instance of the VirtualMachineUpdateParameters class.
    *
    */
    public VirtualMachineUpdateParameters()
    {
        this.configurationSets = new ArrayList<ConfigurationSet>();
        this.dataVirtualHardDisks = new ArrayList<DataVirtualHardDisk>();
    }
}
