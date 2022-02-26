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
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes({"org.sqlbuilder.common.RequiresIdFrom", "org.sqlbuilder.common.ProvidesIdTo"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RequiresProvides extends AbstractProcessor
{
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
	{
		if (annotations.isEmpty())
		{
			processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "no annotation");
		}

		try
		{
			FileObject reportFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "report");
			try (PrintWriter pw = new PrintWriter(reportFile.openWriter()))
			{
				annotations.forEach(annotation -> {

					pw.println(String.format("annotation %s", annotation.getSimpleName()));

					Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);
					elements.stream() //
							.forEach(e -> pw.println(String.format("\t%s %s element '%s' in '%s' astype=[%s]", e.getKind(), e.getClass().getSimpleName(), e, e.getEnclosingElement().getSimpleName(), e.asType().toString())));
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
