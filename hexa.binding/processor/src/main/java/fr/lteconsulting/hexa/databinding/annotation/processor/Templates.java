package fr.lteconsulting.hexa.databinding.annotation.processor;

import java.io.InputStream;
import java.util.Scanner;

public class Templates
{
	private String value;

	public static Templates fromResource( String path )
	{
		Templates result = new Templates();
		
		result.value = readResource( path );
		
		return result;
	}
	
	public static Templates fromResource( String path, int index )
	{
		Templates result = new Templates();
		
		result.value = readResource( path, index );
		
		return result;
	}
	
	public Templates replace( String target, String replacement )
	{
		value = value.replace( target, replacement );
		return this;
	}
	
	@Override
	public String toString()
	{
		return value;
	}

	private static String readResource( String path )
	{
		InputStream is = Templates.class.getClassLoader().getResourceAsStream( path + ".txt" );

		Scanner s = new Scanner( is );
		s.useDelimiter( "\\A" );

		String result = s.hasNext() ? s.next() : "";

		s.close();
		return result;
	}
	
	private static String readResource( String path, int index )
	{
		InputStream is = Templates.class.getClassLoader().getResourceAsStream( path + ".txt" );

		Scanner s = new Scanner( is );
		s.useDelimiter( "------" );

		String result;
		int i = 0;
		do
		{
			if( ! s.hasNext() )
			{
				result = "";
				break;
			}
			
			result = s.next();
			i++;
		}
		while( i < index );

		s.close();
		
		return result;
	}
}
