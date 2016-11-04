package be.ulb.plnmonitor;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.AU;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;

import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class AUOverview extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public AUOverview(final PageParameters parameters, final PLNDataController plnDataController) {
		super(parameters);

		final List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		// TODO: generalize for every PLN id
		List<LOCKSSBox> plnBoxesList = null;
		try {
			plnBoxesList = plnDataController.getPlnDAO().getAllBoxesInfo(0);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<String> allAUNames = null;
		final List<String> allHeaderNames = new ArrayList<String>();
		try {
			allAUNames = plnDataController.getPlnDAO().getAllAUsInPLN(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator<LOCKSSBox> plnBoxesIterator = plnBoxesList.iterator();
		Iterator<String> allAUNamesIterator = allAUNames.iterator();
		allHeaderNames.add("AU Name");
		while (plnBoxesIterator.hasNext()) {
			LOCKSSBox curPlnBox = plnBoxesIterator.next();
			allHeaderNames.add(curPlnBox.getName());
		}

		while (allAUNamesIterator.hasNext()) {
			String curAUName = allAUNamesIterator.next();
			HashMap<String, String> curAUentry = new HashMap<String, String>();
			plnBoxesIterator = plnBoxesList.iterator();
			curAUentry.put("AU Name", curAUName);
			while (plnBoxesIterator.hasNext()) {
				LOCKSSBox curPlnBox = plnBoxesIterator.next();
				try {
					curAUentry.put(curPlnBox.getName(),
							String.valueOf(plnDataController.getPlnDAO().isAUInBox(curPlnBox.getBoxId(), curAUName)));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			data.add(curAUentry);
		}

		// Using a nested ListViews
		add(new ListView<String>("AUlistheader", allHeaderNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = AUOverview.serialVersionUID;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("headerName", String.valueOf(item.getModelObject())));

			}
		});

		add(new ListView<Map<String, String>>("listView", data) {
			/**
			 * 
			 */
			private static final long serialVersionUID = AUOverview.serialVersionUID;

			@Override
			protected void populateItem(ListItem<Map<String, String>> item) {
				final Map<String, String> rowMap = (Map<String, String>) item.getModelObject();

				item.add(new ListView<String>("nested", allHeaderNames) {
					private static final long serialVersionUID = AUOverview.serialVersionUID;

					@Override
					protected void populateItem(ListItem<String> item) {
						final String value = (String) rowMap.get(item.getModelObject());

						if (value.equals("success")) {
							item.add(new Label("value", "<span class=\"label label-success\">Success</span>")
									.setEscapeModelStrings(false));
						} else if (value.equals("warning")) {
							item.add(new Label("value", "<span class=\"label label-warning\">Warning</span>")
									.setEscapeModelStrings(false));
						} else if (value.equals("danger")) {
							item.add(new Label("value", "<span class=\"label label-danger\">Danger</span>")
									.setEscapeModelStrings(false));
						} else if (value.equals("noinfo")) {
							item.add(new Label("value", "<span class=\"label label-info\">No Info</span>")
									.setEscapeModelStrings(false));
						} else {
							// item.add(new Label("value", value));
							Link<String> link = new Link<String>("value") {
								/**
								 * 
								 */
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick() {
									setResponsePage(new AUAcrossBoxesDisplayPage(parameters, plnDataController, value));
								}
							};
							link.setBody(Model.of(value));
							link.add(new AttributeAppender("style", "cursor: pointer;"));
							item.add(link);
						}
					}
				});
			}
		});

		// add(new ListView<Map<String,Map<String,Boolean>>>("listView", data) {
		// @Override
		// protected void populateItem(ListItem<Map<String,Map<String,Boolean>>>
		// item)
		// {
		// final Map<String,Map<String,Boolean>> rowMap = item.getModelObject();
		// item.add
		// (
		// // Wicket: "nested"
		// new ListView<String>("nested", keys)
		// {
		// private static final long serialVersionUID =
		// HomePage.serialVersionUID;
		// @Override
		// protected void populateItem(ListItem<Map<String,Boolean>> item)
		// {
		// String value = rowMap.get(item.getModelObject());
		//
		// // Wicket: "value"
		// item.add(new Label("value", String.valueOf(value)));
		// }
		// }
		// );
		// }
		// });

		// add
		// (
		// // Wicket: "listView"
		// new ListView<Map<String,Integer>>("listView", data)
		// {
		// /** Represents serialVersionUID. */
		// private static final long serialVersionUID =
		// HomePage.serialVersionUID;
		//
		// @Override
		// protected void populateItem(ListItem<Map<String,Integer>> item)
		// {
		// final Map<String,Integer> rowMap = item.getModelObject();
		// item.add
		// (
		// // Wicket: "nested"
		// new ListView<String>("nested", keys)
		// {
		// private static final long serialVersionUID =
		// HomePage.serialVersionUID;
		// @Override
		// protected void populateItem(ListItem<String> item)
		// {
		// Integer value = rowMap.get(item.getModelObject());
		//
		// // Wicket: "value"
		// item.add(new Label("value", String.valueOf(value)));
		// }
		// }
		// );
		// }
		// }
		// );
		//
		// add(new ModelView<LOCKSSBox>("AUlistheader",new
		// ArrayList<String>(data.keySet())) {
		// @Override
		// public void populateItem(final ListItem<String> listItem) {
		//
		// listItem.add(new Label("name"));
		// }
		// }).setVersioned(false);
		//
		// DataView<Entry<String, String>> dataView = new DataView<Entry<String,
		// String>>("displayPanel", new ListDataProvider(new
		// ArrayList<Entry<String, String>(data.entrySet())) {
		// @Override
		// protected void populateItem(Item item) {
		// Entry<String, String> entry = item.getModelObject();
		// item.add(new Label("key_column", entry.getKey()));
		// item.add(new Label("value_column", entry.getValue()));
		// }
		// });

		// add(new PropertyListView<AU>("rows", auList) {
		// @Override
		// public void populateItem(final ListItem<AU> auList) {
		// auList.add(new Label("name"));
		// auList.add(new Label("ContentSize"));
		// }
		// }).setVersioned(false);
		//
		// RepeatingView headers = new RepeatingView("AUlistheader");
		// add(headers);
		// headers.add(new Label(headers.newChildId(),"hello"));
		// headers.add(new Label(headers.newChildId(),"how"));
		// headers.add(new Label(headers.newChildId(),"are"));
		// headers.add(new Label(headers.newChildId(),"you"));
		// headers.add(new Label(headers.newChildId(),"dz"));
		//
		// RepeatingView repeating = new RepeatingView("repeating");
		// add(repeating);
		// RepeatingView columns = new RepeatingView("AUlist_repeat");
		// AbstractItem item;
		// int index = 0;
		// //while (plnBoxes.hasNext())
		// // {
		// //LOCKSSBox box = plnBoxes.next();
		// repeating.add(columns);
		//
		//// Iterator<AU> boxAU = box.getAUs().iterator();
		// for (int row=0; row < 10; row++)
		// {
		//
		// for (index=5*row; index < 5*row+5; index ++) {
		// columns.add(new Label(columns.newChildId(),String.valueOf(index)));
		// }
		//// item = new AbstractItem(repeating.newChildId());
		//// repeating.add(item);
		//// AU curAU = boxAU.next();
		//// item.add(new Label("AUlistbox",
		// curAU.getLockssBox().getIpAddress()));
		//// item.add(new Label("AUlistname", curAU.getName()));
		//// item.add(new Label("AUlisttdbyear", curAU.getTdbYear()));
		//// item.add(new Label("AUlistaccesstype", curAU.getAccessType()));
		//// item.add(new Label("AUlistcontentsize",
		// FileUtils.byteCountToDisplaySize(curAU.getContentSize())));
		//// item.add(new Label("AUlisttdbpublisher", curAU.getTdbPublisher()));
		//// item.add(new Label("AUlistlastpollresult",
		// curAU.getLastPollResult()));

		// final int idx = index;
		// item.add(AttributeModifier.replace("class", new
		// AbstractReadOnlyModel<String>()
		// {
		// private static final long serialVersionUID = 1L;

		// @Override
		// public String getObject()
		// {
		// return (idx % 2 == 1) ? "even" : "odd";
		// }
		// }));

		// index++;
		// }
	}

}

/**
 * 
 */
// class ActionPanel extends Panel
// {
// /**
// *
// */
// private static final long serialVersionUID = -8354021933953685121L;
//
// /**
// * @param id
// * component id
// * @param model
// * model for contact
// */
// public ActionPanel(String id, IModel<Contact> model)
// {
// super(id, model);
// add(new Link("select")
// {
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// @Override
// public void onClick()
// {
// selected = (Contact)getParent().getDefaultModelObject();
// }
// });
// }

/**
 * @return selected contact
 */
// public Contact getSelected()
// {
// return selected;
// }
//
/// **
// * sets selected contact
// *
// * @param selected
// */
// public void setSelected(Contact selected)
// {
// addStateChange();
// this.selected = selected;
// }
//
/// **
// * @return string representation of selected contact property
// */
// public String getSelectedContactLabel()
// {
// if (selected == null)
// {
// return "No Contact Selected";
// }
// else
// {
// return selected.getFirstName() + " " + selected.getLastName();
// }
// }
// }
