/**
 * Copyright (C) 2014 Premium Minds.
 *
 * This file is part of wicket-crudifier.
 *
 * wicket-crudifier is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * wicket-crudifier is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with wicket-crudifier. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.wicket.crudifier.form.elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;

public class EnumControlGroup<T extends Enum<?>> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = -7800336998276030740L;

	private RadioGroup<T> radioGroup;
	
	public EnumControlGroup(String id, IModel<T> model) {
		super(id, model);

		radioGroup = new RadioGroup<T>("radioGroup", getModel());
	}

	@Override
	public FormComponent<T> getFormComponent() {
		return radioGroup;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		try {
			Method method = getType().getMethod("values");
			@SuppressWarnings("unchecked")
			T[] values = (T[]) method.invoke(null);
			
			RepeatingView view = new RepeatingView("repeating");
			for(T value : values){
				Radio<T> radio = new Radio<T>("input", Model.of(value), radioGroup){
					private static final long serialVersionUID = 8903955236018583915L;

					@Override
					public String getValue() {
						return getModel().getObject().name();
					}
					
					@Override
					protected boolean getStatelessHint() {
						return true;
					}
				};
				StringResourceModel stringResourceModel = new StringResourceModel(getPropertyName()+"."+value.name(), getResourceBase(), getModel());
				stringResourceModel.setDefaultValue(value.name());
				
				WebMarkupContainer container = new WebMarkupContainer(view.newChildId());
				container.add(new Label("label", stringResourceModel));
				container.add(radio);
				view.add(container);
			}
			
			radioGroup.add(view);
			
			StringResourceModel stringResourceModel = new StringResourceModel(getPropertyName()+".label", getResourceBase(), getModel());
			stringResourceModel.setDefaultValue(getPropertyName());
			add(new BootstrapControlGroupFeedback("controlGroup")
				.add(radioGroup)
				.add(new Label("label", stringResourceModel))
			);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
