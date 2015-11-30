package com.stereokrauts.stereoscope.controller.interfaces;

/**
 * This is where all controllers derive from.
 * @author th
 *
 * @param <View> The view the controller connects to.
 * @param <Model> The model the controller connects to.
 */
public interface IController<View, Model> {
	View getView();
	void setView(View view);
	
	Model getModel();
	void setModel(Model model);
}
