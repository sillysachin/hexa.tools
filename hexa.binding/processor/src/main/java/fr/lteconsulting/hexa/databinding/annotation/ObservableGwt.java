package fr.lteconsulting.hexa.databinding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation trigger the generation of a child
 * class with all the required getters and setters to
 * be considered as "Observable" :<br/><br/>
 * 
 * - all glue code for generating runtime type information for GWT
 * - there is a getter for each non-private field of the class<br/>
 * - there is a setter for each of the non-private field of the
 * class. Each setter should trigger an event on the
 * binding system.<br/><br/>
 * 
 * The generated class will be generated in the same package
 * as the annotated class and will have the same name, prefixed
 * with "Observable".<br/><br/>
 * 
 * If the annotated class name finishes by "Internal", then the
 * generated class name will have the same name without "Internal".<br/><br/>
 * 
 * So if this annotation is put on a fr.lteconsulting.Person class,
 * it will generate an observable implementation with the following
 * fqn : fr.lteconsulting.ObservablePerson<br/><br/>
 * 
 * @author Arnaud Tournier
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.CLASS )
public @interface ObservableGwt
{
}
