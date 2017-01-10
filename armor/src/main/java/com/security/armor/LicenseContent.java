package com.security.armor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LicenseContent {

	private String name;
	private String macAddr;
	private String type;
	private String validity;
	
	private Map<String,String> properties;
	
	public LicenseContent(String name, String macAddr, String type, String validity) {
		this.name = name;
		this.macAddr = macAddr;
		this.type= type;
		this.validity = validity;
		this.properties = new HashMap<String,String>();
	}

	/**
     * Return an unmodifiable map of properties.
     * 
     * @return
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }
    
    /**
     * Sets the property value.
     * 
     * @param key
     *            the property key
     * @param value
     *            the property value.
     */
    public void setProperty(String key, String value) {
        if (value == null) {
            this.properties.remove(key);
        } else {
            this.properties.put(key, value);
        }
    }
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the macAddr
	 */
	public String getMacAddr() {
		return macAddr;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @return the validity
	 */
	public String getValidity() {
		return validity;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((macAddr == null) ? 0 : macAddr.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof LicenseContent)) {
			return false;
		}
		LicenseContent other = (LicenseContent) obj;
		if (macAddr == null) {
			if (other.macAddr != null) {
				return false;
			}
		} else if (!macAddr.equals(other.macAddr)) {
			return false;
		}
		return true;
	}


	
	
	
	
	
	
}
