//package be.ulb.plnmonitor;
//
//import org.apache.wicket.markup.html.link.Link;
//import org.apache.wicket.markup.html.list.AbstractItem;
//import org.apache.wicket.markup.html.panel.FeedbackPanel;
//import org.apache.wicket.markup.html.panel.Panel;
//
//
//import org.apache.wicket.request.mapper.parameter.PageParameters;
//
//import be.ulb.plnmonitor.datacontroller.PLNDataController;
//import be.ulb.plnmonitor.object.AU;
//import be.ulb.plnmonitor.object.LOCKSSBox;
//import be.ulb.plnmonitor.object.PLN;
//
//import org.apache.wicket.markup.html.basic.Label;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.wicket.AttributeModifier;
//import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
//import org.apache.wicket.markup.repeater.Item;
//import org.apache.wicket.markup.repeater.RepeatingView;
//import org.apache.wicket.model.AbstractReadOnlyModel;
//import org.apache.wicket.model.IModel;
//import org.apache.wicket.model.Model;
//import org.apache.wicket.model.PropertyModel;
//
//
//
//public class AUListPage extends BasePage {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5392655035733062380L;
//	//private static final long serialVersionUID = 1L;
//	
//	
//	public AUListPage(final PageParameters parameters, PLNDataController plnDataController) {
//		super(parameters);
//		 
//	      Iterator<LOCKSSBox> plnBoxes = plnDataController.getPlnDAO().getPlnList().get(0).getPlnBoxes().iterator();
//
//	        RepeatingView repeating = new RepeatingView("repeating");
//	        add(repeating);
//	        AbstractItem item;
//	        int index = 0;
//	        while (plnBoxes.hasNext())
//	        {
//	           
//	            LOCKSSBox box = plnBoxes.next();
//	            
//	            Iterator<AU> boxAU = box.getAUs().iterator();
//	            while (boxAU.hasNext())
//		        {
//	            	
//	            	item = new AbstractItem(repeating.newChildId());
//	            	repeating.add(item);
//	            	AU curAU = boxAU.next();
//	            	item.add(new Label("AUlistbox", curAU.getLockssBox().getIpAddress()));
//	            	item.add(new Label("AUlistname", curAU.getName()));
//	            	item.add(new Label("AUlisttdbyear", curAU.getTdbYear()));
//	            	item.add(new Label("AUlistaccesstype", curAU.getAccessType()));
//	            	item.add(new Label("AUlistcontentsize", FileUtils.byteCountToDisplaySize(curAU.getContentSize())));
//	            	item.add(new Label("AUlisttdbpublisher", curAU.getTdbPublisher()));
//	            	item.add(new Label("AUlistlastpollresult", curAU.getLastPollResult()));
//		       
//			        
//	            final int idx = index;
//	            item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
//	            {
//	                private static final long serialVersionUID = 1L;
//
//	                @Override
//	                public String getObject()
//	                {
//	                    return (idx % 2 == 1) ? "even" : "odd";
//	                }
//	            }));
//
//	            index++;
//		        }
//	        }
//	}
//
//
///**
// * 
// */
////class ActionPanel extends Panel
////{
////    /**
////	 * 
////	 */
////	private static final long serialVersionUID = -8354021933953685121L;
////
////	/**
////     * @param id
////     *            component id
////     * @param model
////     *            model for contact
////     */
////    public ActionPanel(String id, IModel<Contact> model)
////    {
////        super(id, model);
////        add(new Link("select")
////        {
////            /**
////			 * 
////			 */
////			private static final long serialVersionUID = 1L;
////
////			@Override
////            public void onClick()
////            {
////                selected = (Contact)getParent().getDefaultModelObject();
////            }
////        });
////    }
//}
///**
// * @return selected contact
// */
////public Contact getSelected()
////{
////    return selected;
////}
////
/////**
//// * sets selected contact
//// * 
//// * @param selected
//// */
////public void setSelected(Contact selected)
////{
////    addStateChange();
////    this.selected = selected;
////}
////
/////**
//// * @return string representation of selected contact property
//// */
////public String getSelectedContactLabel()
////{
////    if (selected == null)
////    {
////        return "No Contact Selected";
////    }
////    else
////    {
////        return selected.getFirstName() + " " + selected.getLastName();
////    }
////}
////}
//
//