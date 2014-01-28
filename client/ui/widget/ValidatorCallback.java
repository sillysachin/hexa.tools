package com.hexa.client.ui.widget;

public interface ValidatorCallback
{
	public enum Button
	{
		Ok,
		Cancel;
	}

	public void onValidatorAction( Button button );

	public void onValidatorMoveRequest( int dx, int dy );
}
