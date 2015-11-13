package com.nick.behavior;

import java.util.List;

import org.alfresco.repo.node.NodeServicePolicies.OnCreateChildAssociationPolicy;
import org.alfresco.repo.node.NodeServicePolicies.OnDeleteChildAssociationPolicy;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.alfresco.model.ContentModel;

import com.nick.model.MyCoBehaviorModel;
import com.nick.model.MyCoModel;

public class SumExpense implements OnCreateChildAssociationPolicy, OnDeleteChildAssociationPolicy{

	
	private NodeService nodeService;
	private PolicyComponent policyComponent;
	
	// Behaviours
    private Behaviour onCreateChildAssociation;
    private Behaviour onDeleteChildAssociation;
	
	private Logger logger = Logger.getLogger(SumExpense.class);
	
	public void init() {
		if (logger.isDebugEnabled()) logger.debug("Initializing SumExpense behavior");
		
		//Create behaviours
		this.onCreateChildAssociation = new JavaBehaviour(this, "onCreateChildAssociation" , NotificationFrequency.TRANSACTION_COMMIT);
		this.onDeleteChildAssociation = new JavaBehaviour(this, "onDeleteChildAssociation" , NotificationFrequency.TRANSACTION_COMMIT);
		
		//Bind behaviours to node policies
		this.policyComponent.bindAssociationBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateChildAssociation"),
				ContentModel.TYPE_FOLDER,
				this.onCreateChildAssociation);
		this.policyComponent.bindAssociationBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onDeleteChildAssociation"),
				ContentModel.TYPE_FOLDER,
				this.onDeleteChildAssociation);
		
	}
	
	@Override
	public void onCreateChildAssociation(ChildAssociationRef childAssocRef, boolean arg1) {
		if (logger.isDebugEnabled()){
			logger.debug("Inside onCreateChildAssociation");
		}
		
		computeSum(childAssocRef);
	}

	@Override
	public void onDeleteChildAssociation(ChildAssociationRef childAssocRef) {
		if (logger.isDebugEnabled()){
			logger.debug("Inside onDeleteChildAssociation");
		}
		
		computeSum(childAssocRef);
	}
			
	public void computeSum(ChildAssociationRef childAssocRef){
		if (logger.isDebugEnabled()){
			logger.debug("Inside computeSum");
		}
		
		//get the parent node
		NodeRef parentRef = childAssocRef.getParentRef();
		
		//check if parent exists, is an expenseFolder
		if (nodeService.exists(parentRef)){ 
			if (logger.isDebugEnabled()){
				logger.debug("parentRef exists");
			}
		}
		if (nodeService.getType(parentRef).equals(QName.createQName(MyCoBehaviorModel.NAMESPACE_MYCO_BEHAVIOR_MODEL, MyCoBehaviorModel.TYPE_MYB_EXPENSE_FOLDER))){
			
			if (logger.isDebugEnabled()){
				String parentType = nodeService.getType(parentRef).toString();
				logger.debug("parentRef's type matches the type MYB_ExpenseFolder");
				logger.debug("parent type is " + parentType);
			}
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("Parent node isn't an expenseFolder");
				String parentType = nodeService.getType(parentRef).toString();
				logger.debug("parent type is " + parentType);
			}
			return; //ends if fails test
		}
		
		//get all children of the parent
		List<ChildAssociationRef> children = nodeService.getChildAssocs(parentRef);
		
		double expenseSum = 0.0;
		
		//for each child, if it is an expenseReport, get its totalAmount value and add it to a running sum. 
		if(children.size() == 0){
			if (logger.isDebugEnabled()){
				logger.debug("No children found.");
			}
		} else {
			for (ChildAssociationRef child : children){
				if (!child.getTypeQName().equals(QName.createQName(MyCoModel.NAMESPACE_MYCO_CONTENT_MODEL, MyCoModel.TYPE_MY_EXPENSE_REPORT))){
					if (logger.isDebugEnabled()){ 
						logger.debug("Child is not an expense report.");
						String childType = nodeService.getType(child.getChildRef()).toString();
						logger.debug("child type is " + childType);
					}
				} else {
					if (logger.isDebugEnabled()){
						logger.debug("Getting value.");
					}
					
					double expenseValue = 0.0;
					expenseValue = (double)nodeService.getProperty(child.getChildRef(), QName.createQName(MyCoModel.NAMESPACE_MYCO_CONTENT_MODEL, MyCoModel.PROP_MY_TOTAL_AMOUNT));
					
					if (logger.isDebugEnabled()){
						logger.debug("expenseValue: " + expenseValue + " Property value: "
								+ (double)nodeService.getProperty(child.getChildRef(), QName.createQName(MyCoModel.NAMESPACE_MYCO_CONTENT_MODEL, MyCoModel.PROP_MY_TOTAL_AMOUNT)) );
					}
							
					expenseSum += expenseValue;
				}
			}
		}
		
		//Set the sum property on the parent
		nodeService.setProperty(parentRef, QName.createQName(MyCoBehaviorModel.NAMESPACE_MYCO_BEHAVIOR_MODEL,	MyCoBehaviorModel.PROP_MYB_EXPENSE_SUM), expenseSum);
		
		if(logger.isDebugEnabled()){
			String parentName = (String)nodeService.getProperty(parentRef, ContentModel.PROP_NAME);
			logger.debug("Setting expenseSum on " + parentName + "");
			logger.debug("expenseSum = " + expenseSum);
			double value = (double) nodeService.getProperty(parentRef, QName.createQName(MyCoBehaviorModel.NAMESPACE_MYCO_BEHAVIOR_MODEL,	MyCoBehaviorModel.PROP_MYB_EXPENSE_SUM));
			logger.debug(parentName + "'s expenseSum = " + value);
		}
		
		return;
	}
	
	public NodeService getNodeService() {
		return nodeService;
	}


	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}


	public PolicyComponent getPolicyComponent() {
		return policyComponent;
	}


	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

}











