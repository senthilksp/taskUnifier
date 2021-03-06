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
package com.leclercb.taskunifier.api.synchronizer.progress.messages;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;

public class SynchronizerUpdatedModelsProgressMessage extends SynchronizerMainProgressMessage {
	
	private ModelType modelType;
	private int actionCount;
	
	public SynchronizerUpdatedModelsProgressMessage(
			SynchronizerPlugin plugin,
			ProgressMessageType type,
			ModelType modelType,
			int actionCount) {
		super(plugin, type);
		this.setModelType(modelType);
		this.setActionCount(actionCount);
	}
	
	public ModelType getModelType() {
		return this.modelType;
	}
	
	private void setModelType(ModelType modelType) {
		CheckUtils.isNotNull(modelType);
		this.modelType = modelType;
	}
	
	public int getActionCount() {
		return this.actionCount;
	}
	
	private void setActionCount(int actionCount) {
		this.actionCount = actionCount;
	}
	
	@Override
	public String toString() {
		return super.toString()
				+ " : "
				+ this.actionCount
				+ " "
				+ this.modelType;
	}
	
}
