package fr.lteconsulting.hexa.client.ui.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class ImageTextButton extends Widget implements ClickHandler, HasClickHandlers
{
	public interface Callback
	{
		void onClick( Object cookie );
	}

	ImageResource resource;
	String title;

	Element button;

	Object cookie = null;
	Callback callback = null;

	@UiConstructor
	public ImageTextButton( ImageResource resource, String title )
	{
		this.resource = resource;
		this.title = title;

		button = DOM.createButton();
		button.setClassName( "buttonV2" );

		setText( title );

		setElement( button );
	}

	public void setCallback( Callback callback, Object cookie )
	{
		this.callback = callback;
		this.cookie = cookie;

		addClickHandler( this );
	}

	public void setCookie( Object cookie )
	{
		this.cookie = cookie;
	}

	public Object getCookie()
	{
		return cookie;
	}

	public void setText( String text )
	{
		title = text;

		int width = 16;// resource.getWidth()
		int height = 16;// resource.getHeight()
		String elem;
		if( resource != null )
			elem = "<img src='" + resource.getSafeUri().asString() + "' style='width:" + width + "px; height:" + height + "px; position:relative; margin-right:2px;'/>";
		else
			elem = "";
		button.setInnerHTML( elem + text );
		// JQuery.get().jqHtml( button, elem + text );
	}

	@Override
	public HandlerRegistration addClickHandler( ClickHandler handler )
	{
		return addDomHandler( handler, ClickEvent.getType() );
	}

	public void setEnabled( boolean fEnable )
	{
		if( fEnable )
		{
			button.removeAttribute( "disabled" );
			button.removeClassName( "disabled" );
		}
		else
		{
			button.setAttribute( "disabled", "true" );
			button.addClassName( "disabled" );
		}
	}

	@Override
	public void onClick( ClickEvent event )
	{
		assert (callback != null);

		callback.onClick( cookie );
	}
}