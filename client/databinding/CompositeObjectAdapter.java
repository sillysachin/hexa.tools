package com.hexa.client.databinding;

import com.google.gwt.user.client.ui.HasValue;
import com.hexa.client.classinfo.ClazzUtils;
import com.hexa.client.databinding.DataBinding.DataAdapter;
import com.hexa.client.tools.Action1;

public class CompositeObjectAdapter implements DataAdapter
{
	public final static String HASVALUE_TOKEN = "$HasValue";

	private final Object source;
	private final String[] path;

	public CompositeObjectAdapter( Object source, String property )
	{
		this.source = source;
		path = property.split( "\\." );
	}

	class PropertyChangedManager implements Action1<DataBinding.DataAdapter>
	{
		PropertyChangedManager[] managers;
		Action1<DataAdapter> callback;

		String[] path;
		int position;

		DataAdapter adapter;
		Object handler;

		public PropertyChangedManager( String[] path, int position, Action1<DataAdapter> callback, PropertyChangedManager[] managers )
		{
			this.callback = callback;
			this.path = path;
			this.position = position;
			this.managers = managers;
		}

		@SuppressWarnings( "rawtypes" )
		public void register( Object source )
		{
			unregister();

			String pptyName = path[position];

			if( HASVALUE_TOKEN.equals( pptyName ) )
			{
				assert (source instanceof HasValue) : "CompositeObjectAdapter : source is not HasValue : " + source.getClass().getName();
				adapter = new WidgetAdapter( (HasValue) source );
			}
			else
			{
				adapter = new ObjectAdapter( source, path[position] );
			}

			handler = adapter.registerPropertyChanged( this );
			if( handler == null )
			{
				// exec() will never be called, so try to call it now !
				exec( null );
			}
		}

		public void unregister()
		{
			if( adapter != null && handler != null )
				adapter.removePropertyChangedHandler( handler );

			adapter = null;
			handler = null;
		}

		// a change occurs on the watched property
		@Override
		public void exec( DataAdapter param )
		{
			// is that the end ?
			if( position == path.length-1 )
			{
				callback.exec( CompositeObjectAdapter.this );
				return; // that's it
			}

			// get the current value, if not null, register on it
			Object currentValue = getValue( position );
			if( currentValue == null )
			{
				for( int r=position+1; r<managers.length; r++ )
					if( managers[r] != null )
						managers[r].unregister();
				return;
			}

			if( managers[position+1] == null )
				managers[position+1] = new PropertyChangedManager( path, position+1, callback, managers );

			managers[position+1].register( currentValue );

			managers[position+1].exec( param );
		}
	}

	@Override
	public Object registerPropertyChanged( final Action1<DataAdapter> callback )
	{
		PropertyChangedManager[] managers = new PropertyChangedManager[path.length];

		managers[0] = new PropertyChangedManager( path, 0, callback, managers );
		managers[0].register( source );

		return managers;
	}

	@Override
	public void removePropertyChangedHandler( Object handler )
	{
		PropertyChangedManager[] managers = (PropertyChangedManager[]) handler;
		for( int i=0; i<managers.length; i++ )
		{
			managers[i].unregister();
			managers[i] = null;
		}
	}

	private Object getValue( int level )
	{
		Object cur = source;

		for( int i=0; i<=level; i++)
		{
			if( cur == null )
				return null;

			cur = ClazzUtils.GetProperty( cur, path[i] );
		}

		return cur;
	}

	@Override
	public Object getValue()
	{
		return getValue(path.length-1);
	}

	@Override
	public void setValue( Object object )
	{
		Object cur = source;

		if( path.length > 1 )
			cur = getValue( path.length - 2 );

		if( cur == null )
			return;

		ClazzUtils.SetProperty( cur, path[path.length-1], object );
	}
}
