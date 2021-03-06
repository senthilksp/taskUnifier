/*
 * TaskUnifier
 * Copyright (c) 2013, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.TaskList;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.api.models.beans.TaskListBean.TaskItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TaskListBean implements Cloneable, Serializable, Iterable<TaskItemBean> {
	
	@XStreamAlias("tasklist")
	private List<TaskItemBean> tasks;
	
	public TaskListBean() {
		this.tasks = new ArrayList<TaskItemBean>();
	}
	
	@Override
	protected TaskListBean clone() {
		TaskListBean list = new TaskListBean();
		list.tasks.addAll(this.tasks);
		return list;
	}
	
	@Override
	public Iterator<TaskItemBean> iterator() {
		return this.tasks.iterator();
	}
	
	public List<TaskItemBean> getList() {
		return new ArrayList<TaskItemBean>(this.tasks);
	}
	
	public void add(TaskItemBean item) {
		CheckUtils.isNotNull(item);
		this.tasks.add(item);
	}
	
	public void addAll(Collection<TaskItemBean> items) {
		if (items == null)
			return;
		
		for (TaskItemBean item : items)
			this.add(item);
	}
	
	public void remove(TaskItemBean item) {
		CheckUtils.isNotNull(item);
		this.tasks.remove(item);
	}
	
	public void clear() {
		for (TaskItemBean item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.tasks.size();
	}
	
	public int getIndexOf(TaskItemBean item) {
		return this.tasks.indexOf(item);
	}
	
	public TaskItemBean get(int index) {
		return this.tasks.get(index);
	}
	
	public TaskList toTaskGroup() {
		TaskList list = new TaskList();
		
		for (TaskItemBean item : this)
			list.add(item.toTaskItem());
		
		return list;
	}
	
	@XStreamAlias("taskitem")
	public static class TaskItemBean implements Serializable {
		
		@XStreamAlias("task")
		private ModelId task;
		
		@XStreamAlias("link")
		private String link;
		
		public TaskItemBean() {
			this(null, null);
		}
		
		public TaskItemBean(ModelId task, String link) {
			this.setTask(task);
			this.setLink(link);
		}
		
		public ModelId getTask() {
			return this.task;
		}
		
		public void setTask(ModelId task) {
			this.task = task;
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			this.link = link;
		}
		
		public TaskItem toTaskItem() {
			if (this.task == null)
				return new TaskItem(null, this.link);
			
			Task task = TaskFactory.getInstance().get(this.task);
			if (task == null)
				task = TaskFactory.getInstance().createShell(this.task);
			
			return new TaskItem(task, this.link);
		}
		
	}
	
}
