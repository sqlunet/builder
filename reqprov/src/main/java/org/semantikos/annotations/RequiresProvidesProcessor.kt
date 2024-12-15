package org.semantikos.annotations

import java.io.IOException
import java.io.PrintWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@SupportedAnnotationTypes("org.semantikos.annotations.RequiresIdFrom", "org.semantikos.annotations.ProvidesIdTo", "org.semantikos.annotations.ProvidesIdTo2")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
class RequiresProvidesProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {

        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "Annotations " + annotations.size)
        try {
            val msg = processingEnv.messager
            val reportFile = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "report")
            PrintWriter(reportFile.openWriter()).use { pw ->
                annotations.forEach { annotation ->
                    pw.println("annotation ${annotation.simpleName}")
                    //annotation.typeParameters

                    env.getElementsAnnotatedWith(annotation)
                        .asSequence()
                        .forEach {
                            var clazz: String? = null
                            when (annotation.simpleName.toString()) {

                                "RequiresIdFrom" -> {
                                    val reqAnnotation = it.getAnnotation<RequiresIdFrom>(RequiresIdFrom::class.java)
                                    clazz = try {
                                        "type()=" + reqAnnotation.type // this should throw
                                    } catch (mte: MirroredTypeException) {
                                        "type()=" + mte.typeMirror
                                    }
                                    msg.printMessage(Diagnostic.Kind.WARNING, "Requires $clazz")
                                }

                                "ProvidesIdTo"   -> {
                                    val provAnnotation = it.getAnnotation<ProvidesIdTo>(ProvidesIdTo::class.java)
                                    clazz = try {
                                        "type()=" + provAnnotation.type // this should throw
                                    } catch (mte: MirroredTypeException) {
                                        "type()=" + mte.typeMirror
                                    }
                                    msg.printMessage(Diagnostic.Kind.WARNING, "Provides $clazz")
                                }

                                "ProvidesIdTo2"  -> {
                                    val provAnnotation = it.getAnnotation<ProvidesIdTo2>(ProvidesIdTo2::class.java)
                                    clazz = provAnnotation.value
                                    msg.printMessage(Diagnostic.Kind.WARNING, "Provides $clazz")
                                }

                                else             -> {
                                    msg.printMessage(Diagnostic.Kind.WARNING, "No type $annotation")
                                }
                            }

                            pw.println("\t${it.kind} ${it.javaClass.simpleName} $clazz element '$it' in '${it.enclosingElement.simpleName}' astype=[${it.asType()}]")
                        }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }
}
