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
package com.leclercb.taskunifier.gui.api.searchers.filters.conditions.converter;

import com.leclercb.taskunifier.api.models.beans.converters.ContactConverter;
import com.leclercb.taskunifier.api.models.beans.converters.ContextConverter;
import com.leclercb.taskunifier.api.models.beans.converters.FolderConverter;
import com.leclercb.taskunifier.api.models.beans.converters.GoalConverter;
import com.leclercb.taskunifier.api.models.beans.converters.LocationConverter;
import com.leclercb.taskunifier.api.models.beans.converters.MultiConverter;
import com.leclercb.taskunifier.api.models.beans.converters.NoteConverter;
import com.leclercb.taskunifier.api.models.beans.converters.TaskConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.mapper.Mapper;

public class ConditionValueConverter extends MultiConverter {
	
	public ConditionValueConverter(
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		this.addConverter(ContactConverter.INSTANCE);
		this.addConverter(ContextConverter.INSTANCE);
		this.addConverter(FolderConverter.INSTANCE);
		this.addConverter(GoalConverter.INSTANCE);
		this.addConverter(LocationConverter.INSTANCE);
		this.addConverter(NoteConverter.INSTANCE);
		this.addConverter(TaskConverter.INSTANCE);
		this.addConverter(NumberConverter.INSTANCE);
		this.addConverter(StringConverter.INSTANCE);
	}
	
}
