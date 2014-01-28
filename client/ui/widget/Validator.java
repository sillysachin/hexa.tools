package com.hexa.client.ui.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.hexa.client.comm.HexaFramework;

/**
 * @author Arnaud
 * 
 */
public class Validator<T extends Widget> extends Composite implements ClickHandler, KeyUpHandler
{
	private FlexTable m_table = new FlexTable();
	private ImageButton m_okBut = new ImageButton( HexaFramework.images.ok(), "Validate" );
	private ImageButton m_cancelBut = new ImageButton( HexaFramework.images.cancel(), "Cancel modification" );

	private T m_editor = null;
	private ValidatorCallback m_callback = null;

	public Validator()
	{
		m_okBut.addClickHandler( this );
		m_cancelBut.addClickHandler( this );

		initWidget( m_table );

		setEditor( null, true );
	}

	public void setEditor( T editor, boolean fShowCancel )
	{
		m_table.clear();

		m_editor = editor;

		if( m_editor != null )
		{
			m_table.setWidget( 0, 0, m_editor );

			// try to set a key up handler on the editor, if possible
			try
			{
				HasKeyUpHandlers iHasKeyUpHandlers = (HasKeyUpHandlers) m_editor;
				iHasKeyUpHandlers.addKeyUpHandler( this );
			}
			catch( Exception e )
			{
			}

			// try to set focus on the editor, if possible
			try
			{
				Focusable iFocusable = (Focusable) m_editor;
				iFocusable.setFocus( true );
			}
			catch( Exception e )
			{
			}
		}

		m_table.setWidget( 0, 1, m_okBut );
		if( fShowCancel )
			m_table.setWidget( 0, 2, m_cancelBut );
	}

	public T getEditor()
	{
		return m_editor;
	}

	public void setCallback( ValidatorCallback callback )
	{
		m_callback = callback;
	}

	@Override
	public void onClick( ClickEvent event )
	{
		if( m_callback == null )
			return;
		boolean fPrevDefault = true;
		if( event.getSource() == m_okBut )
			m_callback.onValidatorAction( ValidatorCallback.Button.Ok );
		else if( event.getSource() == m_cancelBut )
			m_callback.onValidatorAction( ValidatorCallback.Button.Cancel );
		else
			fPrevDefault = false;
		if( fPrevDefault )
		{
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onKeyUp( KeyUpEvent event )
	{
		if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER )
		{
			m_callback.onValidatorAction( ValidatorCallback.Button.Ok );
		}
		else if( event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE )
		{
			m_callback.onValidatorAction( ValidatorCallback.Button.Cancel );
		}
		/*
		 * else if( event.getCharCode() == KeyCodes.KEY_TAB ) {
		 * m_callback.onValidatorMoveRequest( 1, 0 ); } else if(
		 * event.getCharCode() == KeyCodes.KEY_UP ) {
		 * m_callback.onValidatorMoveRequest( 0, -1 ); } else if(
		 * event.getCharCode() == KeyCodes.KEY_DOWN ) {
		 * m_callback.onValidatorMoveRequest( 0, 1 ); } else if(
		 * event.getCharCode() == KeyCodes.KEY_LEFT ) {
		 * m_callback.onValidatorMoveRequest( -1, 0 ); } else if(
		 * event.getCharCode() == KeyCodes.KEY_RIGHT ) {
		 * m_callback.onValidatorMoveRequest( 1, 0 ); }
		 */
		else
		{
			return;
		}

		event.preventDefault();
	}
}
