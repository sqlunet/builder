package org.sqlbuilder.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes({"org.sqlbuilder.annotations.RequiresIdFrom", "org.sqlbuilder.annotations.ProvidesIdTo", "org.sqlbuilder.annotations.ProvidesIdTo2"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RequiresProvidesProcessor extends AbstractProcessor
{
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
	{
		//if (annotations.isEmpty())
		{
			processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Annotations " + annotations.size());
		}

		try
		{
			FileObject reportFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "report");
			try (PrintWriter pw = new PrintWriter(reportFile.openWriter()))
			{
				annotations.forEach(annotation -> {

					pw.println(String.format("annotation %s", annotation.getSimpleName()));
					annotation.getTypeParameters();
					Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);

					elements.stream() //
							.forEach(e -> {
								String clazz = null;
								if ("RequiresIdFrom".equals(annotation.getSimpleName().toString()))
								{
									RequiresIdFrom reqAnnotation = e.getAnnotation(RequiresIdFrom.class);
									try
									{
										clazz = "type()=" + reqAnnotation.type(); // this should throw
									}
									catch (MirroredTypeException mte)
									{
										clazz = "type()=" + mte.getTypeMirror();
									}
									processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Requires " + clazz);
								}
								else if ("ProvidesIdTo".equals(annotation.getSimpleName().toString()))
								{
									ProvidesIdTo provAnnotation = e.getAnnotation(ProvidesIdTo.class);
									try
									{
										clazz = "type()=" + provAnnotation.type(); // this should throw
									}
									catch (MirroredTypeException mte)
									{
										clazz = "type()=" + mte.getTypeMirror();
									}
									processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Provides " + clazz);
								}
								else if ("ProvidesIdTo2".equals(annotation.getSimpleName().toString()))
								{
									ProvidesIdTo2 provAnnotation = e.getAnnotation(ProvidesIdTo2.class);
									clazz = provAnnotation.value();
									processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Provides " + clazz);
								}
								else
								{
									processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No type " + annotation.toString());
								}
								pw.println(String.format("\t%s %s %s element '%s' in '%s' astype=[%s]", //
										e.getKind(), //
										e.getClass().getSimpleName(), //
										clazz, //
										e, //
										e.getEnclosingElement().getSimpleName(), //
										e.asType().toString()));
							});
				});
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
