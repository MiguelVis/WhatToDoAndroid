/**
 * TaskBook.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.Context;

/**
 * Class that implements a TaskBook under Android
 * 
 * @author  Miguel I. García López
 * @version 1.01
 * @since   2014-10-16
 */
public class TaskBook {

	private WTD wtd;                   // WTD object
	private ArrayList<Task> taskList;  // Tasks list
	
	/**
	 * Constructor
	 * 
	 * @param wtd  WTD object
	 */
	public TaskBook(WTD wtd) {
		
		// Initialize some things
		this.wtd = wtd;
		this.taskList = new ArrayList<Task>();;
	}
	
	/**
	 * Load task list from a file
	 * 
	 * @param fileName  File name
	 * @return true on success, false on failure
	 */
	public boolean loadFile(String fileName) {
		
		ArrayList<Task> tskList = null;  // Tasks list
		BufferedReader br = null;        // Buffered reader
		String bf = null;                // Buffer
		
		// Default result is Success
		boolean result = true;
		
		// Read the file
		try {
			
			// Tasks list
			tskList = new ArrayList<Task>();
			
			// Open
			br = new BufferedReader(new InputStreamReader(wtd.openFileInput(fileName)));
			
			// Read tasks
			while((bf = br.readLine()) != null) {
				
				// Add task to the list
				tskList.add(new Task(bf));
			}
			
		  // Check exceptions
		} catch(Exception e) {
			
			// Failure
			result = false;
			
		  // Do always
		} finally {
			
			// Close the file
			try {
				
				// Close
				br.close();
				
			  // Check exceptions
			} catch(Exception e) {
				
				// Failure
				result = false;
			}
		}
	
		// Set result
		if(result == true)
			this.taskList = tskList;
		
		// Return success or failure
		return result;
	}
	
	/**
	 * Save task list to a file
	 * 
	 * @param fileName  File name
	 * @return true on success, false on failure
	 */
	public boolean saveFile(String fileName) {
		
		// BufferedWriter
		BufferedWriter bw = null;
		
		// Result - success by default
		boolean result = true;
		
		// Write
		try {
			
			// Iterator
			Iterator<Task> it = this.taskList.iterator();

			// Open the file
			bw = new BufferedWriter(new OutputStreamWriter(wtd.openFileOutput(fileName, Context.MODE_PRIVATE)));
			
			// Write the tasks as strings
			while(it.hasNext() == true) {
				
				// Write the task
				bw.write(it.next().toText());
				
				// New line
				bw.newLine();
			}
			
		  // Check exceptions
		} catch(Exception e) {
			
			// Failure
			result = false;
			
		  // Do always
		} finally {
			try {
				
				// Close the file
				bw.close();
				
			  // Check exceptions
			} catch(Exception e) {
				
				// Failure
				result = false;
			}
		}
	
		// Return success or failure
		return result;
	}
	
	/**
	 * Add a task
	 * 
	 * @param tsk  Task to add
	 * @return true
	 */
	public boolean addTask(Task tsk) {
		
		// Add the task
		return this.taskList.add(tsk);
	}
	
	/**
	 * Remove a task
	 * 
	 * @param tsk  Task to remove
	 * @return true on sucess, false on failure
	 */
	public boolean removeTask(Task tsk) {
		
		// Remove the task
		return this.taskList.remove(tsk);
	}
	
	/**
	 * Remove a task
	 * 
	 * @param tskIndex  Index number of the task to remove
	 * @return true on success, false on failure
	 */
	public boolean removeTask(int tskIndex) {
		
		// FIXME
		// Check possible exceptions related to
		// bad index number
		
		// Remove the task
		this.taskList.remove(tskIndex);
		
		// Success
		return true;
	}
	
	/**
	 * Replace a task
	 * 
	 * @param tsk     Old task
	 * @param newTsk  New task
	 * @return true on success, false on failure
	 */
	public boolean replaceTask(Task tsk, Task newTsk) {
		
		// Replace task
		return replaceTask(this.taskList.indexOf(tsk), newTsk);
	}
	
	/**
	 * Replace a task
	 * 
	 * @param tskIndex  Index number of old task
	 * @param newTsk    New task
	 * @return true on success, false on failure
	 */
	public boolean replaceTask(int tskIndex, Task newTsk) {
		
		// FIXME
		// Check possible exceptions related to
		// bad index number
		
		// Replace task
		this.taskList.set(tskIndex, newTsk);
		
		// Success
		return true;
	}
	
	/**
	 * Get the tasks (sorted)
	 * 
	 * @return Tasks
	 */
	public ArrayList<Task> getTaskList() {
		
		// Sort the tasks
		Collections.sort(taskList);

		// Return the tasks
		return this.taskList;
	}
	
}

