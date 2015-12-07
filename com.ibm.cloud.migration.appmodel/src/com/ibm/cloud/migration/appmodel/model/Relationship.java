package com.ibm.cloud.migration.appmodel.model;

public class Relationship {

	/**
	 * @author   chuanran
	 */
	public static enum RelationshipType{
		dependsOn,
		connectTo,
		hostedOn
	}
	RelationshipType type;
	String sourceNode;
	String targetNode;
	
	public Relationship() {
	}
	
	/**
	 * @return
	 */
	public String getTargetNode(){		
		return targetNode;
	}
	
	/**
	 * @return
	 */
	public String getSourceNode(){
		return sourceNode;
	}
	
	
	/**
	 * @return  the type
	 */
	public RelationshipType getType() {
		return type;
	}

	/**
	 * @param type  the type to set
	 */
	public void setType(RelationshipType type) {
		this.type = type;
	}

	/**
	 * @param sourceNode  the sourceNode to set
	 */
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}

	/**
	 * @param targetNode  the targetNode to set
	 */
	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceNode == null) ? 0 : sourceNode.hashCode());
		result = prime * result
				+ ((targetNode == null) ? 0 : targetNode.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relationship other = (Relationship) obj;
		if (sourceNode == null) {
			if (other.sourceNode != null)
				return false;
		} else if (!sourceNode.equals(other.sourceNode))
			return false;
		if (targetNode == null) {
			if (other.targetNode != null)
				return false;
		} else if (!targetNode.equals(other.targetNode))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	
}
