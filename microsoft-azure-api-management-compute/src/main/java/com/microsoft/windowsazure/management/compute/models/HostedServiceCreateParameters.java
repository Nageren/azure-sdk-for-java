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

import java.util.HashMap;

/**
* Parameters supplied to the Create Hosted Service operation.
*/
public class HostedServiceCreateParameters
{
    private String affinityGroup;
    
    /**
    * Required if Location is not specified. The name of an existing affinity
    * group associated with this subscription. This name is a GUID and can be
    * retrieved by examining the name element of the response body returned by
    * the List Affinity Groups operation.  Specify either Location or
    * AffinityGroup, but not both. To list available affinity groups, use the
    * List Affinity Groups operation.
    */
    public String getAffinityGroup() { return this.affinityGroup; }
    
    /**
    * Required if Location is not specified. The name of an existing affinity
    * group associated with this subscription. This name is a GUID and can be
    * retrieved by examining the name element of the response body returned by
    * the List Affinity Groups operation.  Specify either Location or
    * AffinityGroup, but not both. To list available affinity groups, use the
    * List Affinity Groups operation.
    */
    public void setAffinityGroup(String affinityGroup) { this.affinityGroup = affinityGroup; }
    
    private String description;
    
    /**
    * Optional. A description for the cloud service. The description can be up
    * to 1024 characters in length.
    */
    public String getDescription() { return this.description; }
    
    /**
    * Optional. A description for the cloud service. The description can be up
    * to 1024 characters in length.
    */
    public void setDescription(String description) { this.description = description; }
    
    private HashMap<String, String> extendedProperties;
    
    /**
    * Optional. Represents the name of an extended cloud service property. Each
    * extended property must have both a defined name and value. You can have
    * a maximum of 50 extended property name and value pairs.  The maximum
    * length of the Name element is 64 characters, only alphanumeric
    * characters and underscores are valid in the name, and it must start with
    * a letter. Attempting to use other characters, starting with a non-letter
    * character, or entering a name that is identical to that of another
    * extended property owned by the same service, will result in a status
    * code 400 (Bad Request) error.  Each extended property value has a
    * maximum length of 255 characters.
    */
    public HashMap<String, String> getExtendedProperties() { return this.extendedProperties; }
    
    /**
    * Optional. Represents the name of an extended cloud service property. Each
    * extended property must have both a defined name and value. You can have
    * a maximum of 50 extended property name and value pairs.  The maximum
    * length of the Name element is 64 characters, only alphanumeric
    * characters and underscores are valid in the name, and it must start with
    * a letter. Attempting to use other characters, starting with a non-letter
    * character, or entering a name that is identical to that of another
    * extended property owned by the same service, will result in a status
    * code 400 (Bad Request) error.  Each extended property value has a
    * maximum length of 255 characters.
    */
    public void setExtendedProperties(HashMap<String, String> extendedProperties) { this.extendedProperties = extendedProperties; }
    
    private String label;
    
    /**
    * Required. A name for the cloud service that is base-64 encoded. The name
    * can be up to 100 characters in length. The name can be used identify the
    * storage account for your tracking purposes.
    */
    public String getLabel()
    {
        if (this.label == null)
        {
            return this.getServiceName();
        }
        else
        {
            return this.label;
        }
    }
    
    /**
    * Required. A name for the cloud service that is base-64 encoded. The name
    * can be up to 100 characters in length. The name can be used identify the
    * storage account for your tracking purposes.
    */
    public void setLabel(String label) { this.label = label; }
    
    private String location;
    
    /**
    * Required if AffinityGroup is not specified. The location where the cloud
    * service will be created.  Specify either Location or AffinityGroup, but
    * not both. To list available locations, use the List Locations operation.
    */
    public String getLocation() { return this.location; }
    
    /**
    * Required if AffinityGroup is not specified. The location where the cloud
    * service will be created.  Specify either Location or AffinityGroup, but
    * not both. To list available locations, use the List Locations operation.
    */
    public void setLocation(String location) { this.location = location; }
    
    private String serviceName;
    
    /**
    * Required. A name for the cloud service that is unique within Windows
    * Azure. This name is the DNS prefix name and can be used to access the
    * service.
    */
    public String getServiceName() { return this.serviceName; }
    
    /**
    * Required. A name for the cloud service that is unique within Windows
    * Azure. This name is the DNS prefix name and can be used to access the
    * service.
    */
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    /**
    * Initializes a new instance of the HostedServiceCreateParameters class.
    *
    */
    public HostedServiceCreateParameters()
    {
        this.extendedProperties = new HashMap<String, String>();
    }
}
