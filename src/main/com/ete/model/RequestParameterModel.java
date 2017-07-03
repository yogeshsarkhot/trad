package main.com.ete.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestParameterModel {

	private OrganizationModel organizationModel;
	private UserModel userModel;
	private DataEntityModel dataEntityModel;
	private RequirementModel requirementModel;
	private UserModel accessUserModel;

	public RequestParameterModel() {
	}

	public OrganizationModel getOrganizationModel() {
		return organizationModel;
	}

	public void setOrganizationModel(OrganizationModel organizationModel) {
		this.organizationModel = organizationModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public DataEntityModel getDataEntityModel() {
		return dataEntityModel;
	}

	public void setDataEntityModel(DataEntityModel dataEntityModel) {
		this.dataEntityModel = dataEntityModel;
	}

	public UserModel getAccessUserModel() {
		return accessUserModel;
	}

	public void setAccessUserModel(UserModel accessUserModel) {
		this.accessUserModel = accessUserModel;
	}

	public RequirementModel getRequirementModel() {
		return requirementModel;
	}

	public void setRequirementModel(RequirementModel requirementModel) {
		this.requirementModel = requirementModel;
	}
}
