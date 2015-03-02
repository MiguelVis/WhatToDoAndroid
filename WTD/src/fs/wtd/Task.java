/**
 * Task.java
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

/**
 * Class that implements a Task object
 * 
 * @author  Miguel I. García López
 * @version 1.00
 * @since   2014-10-16
 */
public class Task implements Comparable<Task> {
	
	// Value for no priority
	public static final int TASK_PRIORITY_NONE = 99;
	
	// Task fields
	private boolean done = false;              // Done (true / false)
	private int priority = TASK_PRIORITY_NONE; // Priority (0...X / 99)
	private String date = null;                // Date ('YYYY-MM-DD')
	private String subject = null;             // Subject
		
	/**
	 * Constructor
	 */
	public Task() {
		
		// Nothing to do
	}
	
	/**
	 * Constructor giving the fields from a String
	 * 
	 * @param task  Task description
	 */
	public Task(String task) {
		
		// Index to look at the string
		int index = 0;
		
		// Done field
		if(task.charAt(index) == 'x' && task.charAt(index + 1) == ' ') {
			
			this.done = true; // Done is true
			index += 2;       // Next field
		}
		
		// Priority field
		if(task.charAt(index) == '('
			&& Character.isLetter(task.charAt(index + 1)) == true
			&& Character.isUpperCase(task.charAt(index + 1)) == true
			&& task.charAt(index + 2) == ')'
			&& task.charAt(index + 3) == ' ') {
				
				this.priority = task.charAt(index + 1) - 'A'; // Set priority
				index +=4;                                    // Next field
		}
		
		// Date field
		if(Character.isDigit(task.charAt(index)) == true
			&& Character.isDigit(task.charAt(index + 1)) == true
			&& Character.isDigit(task.charAt(index + 2)) == true
			&& Character.isDigit(task.charAt(index + 3)) == true
			&& task.charAt(index + 4) == '-'
			&& Character.isDigit(task.charAt(index + 5)) == true
			&& Character.isDigit(task.charAt(index + 6)) == true
			&& task.charAt(index + 7) == '-'
			&& Character.isDigit(task.charAt(index + 8)) == true
			&& Character.isDigit(task.charAt(index + 9)) == true
			&& task.charAt(index + 10) == ' ') {
			
				this.date = task.substring(index, index + 10); // Set date
				index +=11;                                    // Next field				
			}
		
		// Subject field
		this.subject = task.substring(index);
	}
	
	/**
	 * Set done field
	 * 
	 * @param done  True if done, else false
	 */
	public void setDone(boolean done) {
		
		// Set done field
		this.done = done;
	}
	
	/**
	 * Get done field
	 * 
	 * @return True if done, else false
	 */
	public boolean getDone() {
		
		// Return done field
		return this.done;
	}

	/**
	 * Set priority field
	 * 
	 * @param priority  Priority
	 */
	public void setPriority(int priority) {
		
		// Set priority field
		this.priority = priority;
	}
	
	/**
	 * Return priority field
	 * 
	 * @return Priority
	 */
	public int getPriority() {
		
		// Return priority field
		return this.priority;
	}
	
	/**
	 * Set date field
	 * 
	 * @param date  Date
	 */
	
	public void setDate(String date) {
		
		// Set date field
		this.date = date;
	}
	
	/**
	 * Get date field
	 * 
	 * @return Date as string
	 */
	
	public String getDate() {
		
		// Get date field
		return this.date;
	}
	
	/**
	 * Set subject field
	 * 
	 * @param subject  Subject
	 */
	public void setSubject(String subject) {
		
		// Set subject field
		this.subject = subject;
	}
	
	/**
	 * Get subject field
	 * 
	 * @return Subject
	 */
	public String getSubject() {
		
		// Return subject
		return this.subject;
	}
	
	/**
	 * Returns task as a string
	 * 
	 * @return Task as a string
	 */
	public String toText() {
		
		// String to return
		String s = new String();
		
		// Done field
		if(this.done == true)
			s = s.concat("x ");
		
		// Priority field
		if(this.priority != 99)
			s = s.concat("(" + Character.toString((char) ('A' + priority)) + ") ");
		
		// Date field
		if(this.date != null)
			s = s.concat(this.date + " ");
		
		// Subject field
		if(this.subject != null)
			s = s.concat(this.subject);
		
		// Return string
		return s;		
	}
	
	/**
	 * Compare method for Collections.sort
	 */
	public int compareTo(Task anotherTask) {
		
		// Compare
		
        return this.toText().compareTo(anotherTask.toText());
    }
	
}

