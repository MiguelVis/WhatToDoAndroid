/*
 * WTD.java
 *
 * Copyright (c) 2015 Miguel I. Garcia Lopez / FloppySoftware, Spain
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA. 
 */
package fs.wtd;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Main class of WTD.
 * 
 * @author  Miguel I. García López
 * @version 1.00
 * @since   27 Feb 2015
 */
public class WTD extends ActionBarActivity {
	
	// Name of the file name that stores the tasks
	static private final String FILENAME = "todo.txt";
	
	// Values for dialogInput - Dialog types
	static private final int INPUT_TYPE_NEW  = 0; // New task
	static private final int INPUT_TYPE_EDIT = 1; // Edit task
	
	// Needed to add the tasks as TextViews programmatically
	private LinearLayout layoutLines;  // Layout
	private ArrayList<TextView> views; // TextViews
	
	// The tasks
	private TaskBook book;			// Tasks book
	private ArrayList<Task> tasks;	// Tasks list
	
	// Selected task number in a context menu
	private int selectedTask;

	/**
	 * Method called when the application starts
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//
		super.onCreate(savedInstanceState);
		
		// Set view
		setContentView(R.layout.activity_wtd);
		
		// According to Google: The use of application icon plus title as
		// a standard layout is discouraged on API 21 devices and newer
		
		// Force to show icon in the ActionBar
		ActionBar ab = getSupportActionBar(); 
	    ab.setDisplayShowHomeEnabled(true);
	    ab.setIcon(R.drawable.ic_launcher);
		
		// Initialize some data
		
	    // Needed to add the tasks as TextViews programmatically
		layoutLines = (LinearLayout) findViewById(R.id.LinearLayoutLines);
		views = new ArrayList<TextView>();
		
		// Our task book
		book = new TaskBook(this);
		
		// Read the task book file
		if(book.loadFile(FILENAME) == false) {
			
			// No tasks loaded
			
			// Task t = new Task("No tasks");
			// book.addTask(t);
		}
		
		// Show the tasks on screen
		refresh();
	}

	/**
	 * Method called when the menu is created
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu
		
		// This adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.wtd, menu);
		
		return true;
	}

	/**
	 * Method called when an option in the action bar is selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Get the option id
		int id = item.getItemId();
		
		// Do an action according to the selected option
		switch(id) {
		
			// New task
			case R.id.action_new :
				dialogInput(INPUT_TYPE_NEW, null);
				return true;
				
			// Remove all tasks that are marked as done
			case R.id.action_removeAllDoneTasks :
				removeAllDoneTasks();
				return true;
				
			// Settings
			case R.id.action_settings :
				return true;
				
			// About of WTD
			case R.id.action_about :
				dialogAbout();
				return true;
		}
		
		//
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Method called when the context menu is created
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		// Get view tag to know what task is
		String s = (String) v.getTag();
		
		// Set the variable value with the corresponding task number,
		// to help the context menu options to do their job
		selectedTask = Integer.parseInt(s.substring(4));
		
		// Set the ContextMenu title
		menu.setHeaderTitle(tasks.get(selectedTask).toText());
		
		// Create the ContextMenu
		super.onCreateContextMenu(menu, v, menuInfo);
		
		// Inflate the ContextMenu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/**
	 * Method called when an option in the context menu is selected
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		// Get the selected task
		Task task = tasks.get(selectedTask);
		
		// Get the option id
		int id = item.getItemId();
		
		// Do an action according to the selected option
		switch (id) {
		
			// Edit task
			case R.id.context_edit:
				
				// Edit dialog
				dialogInput(INPUT_TYPE_EDIT, task);
				
				// FIXME
				// Check if selectedTask has changed before save and refresh?
				
				// Save the tasks
				save();
				
				// Refresh the tasks on screen
				refresh();
				
				// Done
				return true;
			
			// Remove task
			case R.id.context_remove:

				// Remove the task
				book.removeTask(task);
				
				// Save the tasks
				save();
				
				// Refresh the tasks on screen
				refresh();
				
				// Done
				return true;
			
			// Mark task as done
			case R.id.context_done :
				
				// Mark task as done if it is not already done
				if(task.getDone() == false) {
					
					// Mark task as done
					task.setDone(true);
					
					// Save the tasks
					save();
					
					// Refresh the tasks on screen
					refresh();
				}
				
				// Done
				return true;
				
			// Mark task as undone
			case R.id.context_undone :
				
				// Mark task as undone if it is not already undone
				if(task.getDone() == true) {
					
					// Mark task as undone
					task.setDone(false);
					
					// Save the tasks
					save();
					
					// Refresh the tasks on screen
					refresh();
				}
				
				// Done
				return true;				
				
			//
			default:
				return false;
		}
	}
	
	/**
	 * Save the tasks book on disk
	 */
	private void save() {
		
		// Save
		book.saveFile(FILENAME);
	}
	
	/**
	 * Refresh the tasks on screen
	 */
	private void refresh() {
		
		// Get the task list from the tasks book
		tasks = book.getTaskList();
		
		// Remove the current tasks views in the layout
		if(views.isEmpty() == false) {
			
			// Remove all tasks views
			for(View v : views) {
				
				// Remove view
				((LinearLayout) v.getParent()).removeView(v);
			}
			
			// Clear the views list
			views.clear();
		}
		
		// Get padding values from the header
		TextView textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		
		int paddingTop = textViewHeader.getPaddingTop();
		int paddingBottom = textViewHeader.getPaddingBottom();
		int paddingLeft = textViewHeader.getPaddingLeft();
		int paddingRight = textViewHeader.getPaddingRight();
		
		// Build new views, one for each task
		int index = 0;
		
		for(Task t : tasks) {
			
			// Build a formatted string matching with the header
			
			// 'D Pri Date       Task'
			// 'x (A) 2015-01-25 Call Elvis'
			
			// String for the task text
			String s;
			
			// Done field
			s = (t.getDone() == true) ? "x " : "  ";
			
			// Priority field
			if(t.getPriority() != Task.TASK_PRIORITY_NONE)
				s = s.concat("(" + Character.toString((char) ('A' + t.getPriority())) + ") ");
			else
				s = s.concat("    ");
			
			// Date field
			if(t.getDate() != null)
				s = s.concat(t.getDate() + " ");
			else
				s = s.concat("           ");
			
			// Subject field
			if(t.getSubject() != null)
				s = s.concat(t.getSubject());
			
			// Build TextView
			TextView v = new TextView(this);
			
			// Enable the context menu
			registerForContextMenu(v);
			
			// Set tag to help the context menu options to do their job
			v.setTag("TASK" + index);

			// Set layout parameters
			v.setLayoutParams(new ViewGroup.LayoutParams(
			        ViewGroup.LayoutParams.MATCH_PARENT,
			        ViewGroup.LayoutParams.WRAP_CONTENT));
			
			// Set padding
			v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
			
			// Display only a line of text if overflows the right margin
			v.setSingleLine();

			// Set text
			v.setText(s);
			
			// Set monospace font to match with the header
			v.setTypeface(Typeface.MONOSPACE);
			
			// Set text color
			v.setTextColor(Color.BLACK);
			
			// Set a different background color for odd lines
			if(index % 2 > 0) {
				
				// Set background color
				v.setBackgroundColor(Color.LTGRAY);
			}
			
			// Add view to layout
			layoutLines.addView(v);
			
			// Add view to ArrayList
			views.add(v);
			
			// Next line index
			++index;
		}
	}
	
	/**
	 * Remove all tasks marked as done
	 */
	private void removeAllDoneTasks() {
		
		// FIXME
		// Show a dialog giving the chance to cancel
		
		// Get an iterator
		Iterator<Task> it = tasks.iterator();

		// Test all the tasks
		while(it.hasNext()) {
			
			// Get task
		    Task t = it.next();

		    // Remove the task if it is marked as done
		    if(t.getDone() == true) {
		    	
		    	// Remove it
		        it.remove();
		    }
		}
		
		/* ************************************************************
		   THIS CAUSES 'ConcurrentModificationException'
		   SO, WE USE ABOVE PROCEDURE
		 
		// Test all the tasks
		for(Task t : tasks) {
			
			// Remove the task if it is marked as done
			if(t.getDone() == true) {
				
				// Remove it
				book.removeTask(t);
			}
		}
		
		************************************************************ */
		
		// Save the task book on disk
		save();
		
		// Refresh the tasks on screen
		refresh();
	}

	/**
	 * Create or edit a task
	 * 
	 * @param type  Operation type (create or edit)
	 * @param task  Task if edit operation, else null
	 */
	public void dialogInput(int type, Task task) {
		
		// Dialog type
		final int inputType = type;
		
		// Task on edit, null on create
		final Task inputTask = task;
		
		// Create dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); 
		
		// Set view 
		LayoutInflater layoutInflater = LayoutInflater.from(this); 
		View promptView = layoutInflater.inflate(R.layout.input, null);
	
		// Set dialog layout
		alertDialogBuilder.setView(promptView); 
	
		// Subject field
		final EditText input = (EditText) promptView.findViewById(R.id.input_text);
		
		// Priority field
		final Spinner spinnerPriority = (Spinner) promptView.findViewById(R.id.input_spinnerPriority);
		
		// Date field
		final DatePicker datePicker = (DatePicker) promptView.findViewById(R.id.input_datePicker);
		
		// CheckBox for date
		final CheckBox checkBoxUndated = (CheckBox) promptView.findViewById(R.id.input_checkBoxDate);
		
		// OnClickListener for the CheckBox
		checkBoxUndated.setOnClickListener(new OnClickListener() {
			
			// Enable the DatePicker if the CheckBox is not checked (dated task)
			// Disable the DatePicker if the CheckBox is checked (undated task)
			@Override
			public void onClick(View v) {
				
				//
				datePicker.setEnabled(checkBoxUndated.isChecked() ? false : true );
			}
		});

		// Set the field values on edit task option
		if(inputType == INPUT_TYPE_EDIT) {
			
			// Edit task
			
			// Priority field
			int position = inputTask.getPriority();
			
			// Set priority value
			if(position == Task.TASK_PRIORITY_NONE) {
				
				position = 3;  // None
				
			} else if(position > 2) {
				
				position = 2;  // Low priority
			}
			
			// Set priority level in spinner
			spinnerPriority.setSelection(position);
			
			// Date field
			String d = inputTask.getDate();
			
			// Convert date if any
			if(d != null) {
				
				// Task has date
				checkBoxUndated.setChecked(false);
				
				// YYYY-MM-DD
				// 0123456789
				
				// Parse date
				int year  = Integer.parseInt(d.substring(0, 4));
				int month = Integer.parseInt(d.substring(5, 7)) - 1;  // January is 0
				int day   = Integer.parseInt(d.substring(8,10));
				
				// Set picker date
				datePicker.updateDate(year, month, day);
				
			} else {
				
				// Task has not date
				checkBoxUndated.setChecked(true);
				
				// Disable DatePicker
				datePicker.setEnabled(false);
			}
			
			// Subject field
			input.setText(inputTask.getSubject());
			
			// Done field : We'll see later
			
		} else {
			
			// New task
			
			// Set the field values on new task option
			
			// Priority field : NONE
			spinnerPriority.setSelection(3);
			
			// Subject field : EMPTY
			input.setText("");
			
			// Task has not date
			checkBoxUndated.setChecked(true);
			
			// Disable DatePicker
			datePicker.setEnabled(false);
			
			// Date : TODAY by default
			
			// Done field : We'll see later
		}
	
		// Setup dialog title
		if(type == INPUT_TYPE_NEW) {
			
			// New task
			alertDialogBuilder.setTitle(getResources().getString(R.string.input_new));
			
		} else {
			
			// Edit task
			alertDialogBuilder.setTitle(getResources().getString(R.string.input_edit));
		}
		
		// Create dialog
		alertDialogBuilder
		
			//
			.setCancelable(false) 
			
			// OK button
			.setPositiveButton(getResources().getString(R.string.input_ok),
					
				// OnClick listener
				new DialogInterface.OnClickListener() { 
	
					//
					public void onClick(DialogInterface dialog, int id) { 
		
						// Check if input is valid
						if(input.getText().toString().length() > 0) {
			 
							// Create task
							Task t = new Task();
							
							// Subject field							
							t.setSubject(input.getText().toString());
							
							// Priority field
							int priority = spinnerPriority.getSelectedItemPosition();
							
							// Adjust value if priority is NONE
							if(priority == 3)
								priority = Task.TASK_PRIORITY_NONE;
							
							// Set the task priority
							t.setPriority(priority);
							
							// Date field
							if(checkBoxUndated.isChecked() == true) {
								
								// Task has no date
								t.setDate(null);
								
							} else {
								
								// Task has date
								String d = String.format("%04d-%02d-%02d", 
										datePicker.getYear(),
										datePicker.getMonth() + 1, // January is 0 in the DatePicker
										datePicker.getDayOfMonth());
								
								// Set task date
								t.setDate(d);
							}
							
							// Do action according to the dialog type
							if(inputType == INPUT_TYPE_NEW) {
								
								// New task
								
								// Done field : FALSE
								t.setDone(false);
								
								// Add task
								book.addTask(t);
								
							} else {
								
								// Replace task
								
								// Done field : same as before
								t.setDone(inputTask.getDone());
								
								// Edit : Replace task
								book.replaceTask(inputTask,t);
							}
							
							// Save the task book on disk
							save();
							
							// Refresh the tasks on screen
							refresh();
						}
					} 
			}) 
	
			// Cancel button
			.setNegativeButton(getResources().getString(R.string.input_cancel), 
	
					// OnClick listener
					new DialogInterface.OnClickListener() { 
	
						//
						public void onClick(DialogInterface dialog, int id) { 
	
							// Cancel
							dialog.cancel(); 
						} 
			}); 
	
			// Create AlertDialog
			AlertDialog alertD = alertDialogBuilder.create(); 
	
			// Show the dialog
			alertD.show(); 
	}
	
	/**
	 * Show the About Of dialog
	 */
	private void dialogAbout() {
		
		// Create the dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Build the dialog
		builder.setTitle(getResources().getString(R.string.about_title))
			   .setMessage(getResources().getString(R.string.about_text))
		       .setCancelable(false)
		       .setPositiveButton(getResources().getString(R.string.input_ok),
		    		   
		    		   new DialogInterface.OnClickListener() {
		    	   
		    	   		public void onClick(DialogInterface dialog, int id) {
		    	   			
		                // Nothing to do
		           }
		       });
		
		// Create AlertDialog
		AlertDialog alert = builder.create();
		
		// Show the dialog
		alert.show();
	}

}


