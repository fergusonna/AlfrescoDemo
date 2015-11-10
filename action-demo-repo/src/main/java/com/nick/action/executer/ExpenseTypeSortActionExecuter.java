package com.nick.action.executer;

import java.util.ArrayList;
import java.util.List;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.alfresco.model.ContentModel;
import com.nick.model.MyCoModel;
import com.nick.model.MyCoBehaviorModel;
/**
 * @author Nick Ferguson
 *
 */
public class ExpenseTypeSortActionExecuter extends ActionExecuterAbstractBase{
	
	public static final String NAME = "expense-type-sort";
	
	private FileFolderService fileFolderService;
	private NodeService nodeService;
	private Logger logger = Logger.getLogger(ExpenseTypeSortActionExecuter.class);
	
	@Override
	protected void executeImpl(Action ruleAction, NodeRef node) {
		/**********************************************************
		 * Plan: When node enters folder, check if node is an expenseReport, 
		 * if yes, then check the expenseType
		 * 
		 * Place into subfolder for corresponding expenseType 
		 * 		(if it doesn't exist, create it)
		 *********************************************************/
		if (logger.isDebugEnabled()){
			logger.debug("I'm going to do a thing.");
		}
		//get node's type and check if it is an expense report
		if(!nodeService.getType(node).isMatch(QName.createQName(MyCoModel.NAMESPACE_MYCO_CONTENT_MODEL, MyCoModel.TYPE_MY_EXPENSE_REPORT))){
			if(logger.isDebugEnabled()){
				logger.debug("Node is not an expense report");
			}
			//if !expenseReport: end
			return;
		} 
		if(logger.isDebugEnabled()){
			logger.debug("Node is an expense report");
		}
		
		if(!nodeService.exists(node)){
			if (logger.isDebugEnabled()){ logger.debug("Node doesn't exist, what do?"); };
			return;
		}
		if(nodeService.exists(node) && logger.isDebugEnabled()){
			logger.debug("Node exists");
		}
		
		//get expenseType
		ArrayList<?> expenseTypeList = (ArrayList<?>) nodeService.getProperty(node, QName.createQName(MyCoModel.NAMESPACE_MYCO_CONTENT_MODEL, MyCoModel.PROP_MY_EXPENSE_TYPE));
		
		String expenseType = expenseTypeList.get(0).toString();
		if(logger.isDebugEnabled()){
			logger.debug("expenseType is: " + expenseTypeList.get(0).toString());
		}
		
		//get node parent, check if folder exists with the same name as the expenseType
		ChildAssociationRef parentRef = nodeService.getPrimaryParent(node);
		NodeRef parent = parentRef.getParentRef();
		
		List<ChildAssociationRef> children = nodeService.getChildAssocs(parent);
		boolean nodeMoved = false;
		
		if (children.size() == 0){
			if(logger.isDebugEnabled()){
				logger.debug("No Children Found");
			}
		} else {
			for(ChildAssociationRef child: children){
//				if (!child.getTypeQName().isMatch(ContentModel.TYPE_FOLDER)
//					NodeRef childRef = child.getChildRef();
//					String childName = nodeService.getProperty(childRef, ContentModel.PROP_NAME).toString();
//					
//					String childType = (String) nodeService.getType(childRef).toString();
//					
//					if(logger.isDebugEnabled()){
//						logger.debug("Child name is: " + childName);
//						logger.debug("ContentModel.TYPE_FOLDER = " + ContentModel.TYPE_FOLDER);						
//						logger.debug("Child type is: " + childType);
//					}
//					if(logger.isDebugEnabled()){
//						logger.debug("Child is not a folder.");
//					}
//					
//				}
				//get name of folder:
				NodeRef childRef = child.getChildRef();
				String childName = nodeService.getProperty(childRef, ContentModel.PROP_NAME).toString();
				if(logger.isDebugEnabled()){
					logger.debug("Child name is: " + childName + "\n, checking if that is same as: " + expenseType);
				}
				if(childName.equals(expenseType)){
					//move node into folder
					if(logger.isDebugEnabled()){
						logger.debug("Child is a folder with name equal to expense type.");
					}
					try {
						fileFolderService.move(node, childRef, null);
						if(logger.isDebugEnabled()){
							logger.debug("File moved");
						}
					} catch (FileExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(logger.isDebugEnabled()){
							logger.debug("File already exists in subfolder");
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(logger.isDebugEnabled()){
							logger.debug("File not found exception");
						}
					}
					nodeMoved = true;
					break;
				}
			}
		}
		
		//If the node wasn't moved to a subfolder
		if (!nodeMoved){
			//create a subfolder with the name of the expense type
			if(logger.isDebugEnabled()){
				logger.debug("Child wasn't moved, folder needs to be created.");
			}
			NodeRef expenseFolder = fileFolderService.create(parent, expenseType, QName.createQName(MyCoBehaviorModel.NAMESPACE_MYCO_BEHAVIOR_MODEL, MyCoBehaviorModel.TYPE_MYB_EXPENSE_FOLDER)).getNodeRef();
			nodeService.setProperty(expenseFolder, QName.createQName(MyCoBehaviorModel.NAMESPACE_MYCO_BEHAVIOR_MODEL, MyCoBehaviorModel.PROP_MYB_EXPENSE_SUM), 0.0);
			if(logger.isDebugEnabled()) {
				logger.debug("Expense Folder is created, and the property expeseSum was added.");
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("expenseFolder created with nodeRef: " + expenseFolder.toString());
			}
			//move node to the subfolder
			try {
				fileFolderService.move(node, expenseFolder, null);
				if(logger.isDebugEnabled()){
					logger.debug("File moved");
				}
			} catch (FileExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(logger.isDebugEnabled()){
					logger.debug("File already exists in subfolder");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(logger.isDebugEnabled()){
					logger.debug("File not found exception");
				}
			}
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Reached end of ExpenseTypeSortActionExecuter, Here's hoping it worked.");
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> arg0) {
		
	}
	
	public void setNodeService(NodeService nodeService){
		this.nodeService = nodeService;
	}
	public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }
}
