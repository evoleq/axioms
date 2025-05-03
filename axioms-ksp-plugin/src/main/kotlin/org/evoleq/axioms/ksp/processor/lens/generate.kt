package org.evoleq.axioms.ksp.processor.lens

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.OutputStreamWriter

fun generateLensFile(classDecl: KSClassDeclaration, codeGenerator: CodeGenerator, logger: KSPLogger) {
    val packageName = classDecl.packageName.asString()
    val className = classDecl.simpleName.asString()
    val fileName = "${className}Lenses"

    val file = codeGenerator.createNewFile(
        Dependencies(false, classDecl.containingFile!!),
        packageName,
        fileName
    )

    OutputStreamWriter(file, Charsets.UTF_8).use { writer ->
        writer.write("package $packageName\n\n")
        writer.write("import org.evoleq.axioms.structure.lens.diagonal.Lens\n")
        writer.write("import org.evoleq.axioms.ksp.definition.FORBIDDEN\n")
        writer.write("import $packageName.$className\n\n")

        for (property in classDecl.getAllProperties()) {
            val name = property.simpleName.asString()
            val resolvedType = property.type.resolve()
            val typeName = resolvedType.declaration.qualifiedName?.asString() ?: "UNKNOWN"

            val isReadOnly = property.annotations.any {
                it.shortName.asString() == "ReadOnly"
            }

            val isReadWrite = property.annotations.any {
                it.shortName.asString() == "ReadWrite"
            }

            when {
                isReadOnly -> {
                    writer.write(
                        """
                            val $name: Lens<$className, $typeName> by lazy {
                                Lens(
                                    get = { w -> w.$name },
                                    set = FORBIDDEN("Property '$name' is read-only")
                                )
                            }

                            """.trimIndent()
                    )
                }
                isReadWrite -> {
                    writer.write(
                        """
                            val $name: Lens<$className, $typeName> by lazy {
                                Lens(
                                    get = { w -> w.$name },
                                    set = { p -> { w -> w.copy($name = p) } }
                                )
                            }

                            """.trimIndent()
                    )
                }
            }
        }
    }
}
