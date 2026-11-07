rootProject.name = "OpenModelSphere"

// ─────────────────────────────────────────────────────────────
// Modules CŒUR (à migrer en priorité car dépendances critiques)
// ─────────────────────────────────────────────────────────────
include(":org.modelsphere.jack")           // Framework/plugin core
include(":org.modelsphere.sms")            // Semantic Modeling System (cœur métier)
include(":org.modelsphere.distribution")   // Packaging
include(":org.modelsphere.guide")          // Documentation

// ─────────────────────────────────────────────────────────────
// Plugins Neosapiens (dont codegen en premier)
// ─────────────────────────────────────────────────────────────
include(":com.neosapiens.plugins.codegen")
include(":com.neosapiens.plugins.diagramming.neighbors")
include(":com.neosapiens.plugins.diagramming.stretch")
include(":com.neosapiens.plugins.html.browser")
include(":com.neosapiens.plugins.layout")
include(":com.neosapiens.plugins.reverse.java")
include(":com.neosapiens.plugins.reverse.src.java")

// ─────────────────────────────────────────────────────────────
// Plugins ModelSphere (à ajouter progressivement)
// ─────────────────────────────────────────────────────────────
include(":org.modelsphere.plugins.ansi_forward")
include(":org.modelsphere.plugins.ansi_forward_toolkit")
include(":org.modelsphere.plugins.diagram.statistics")
include(":org.modelsphere.plugins.diagram.tree")
include(":org.modelsphere.plugins.export.links")
include(":org.modelsphere.plugins.getdomainvalues")
include(":org.modelsphere.plugins.propagatedomainvalues")
include(":org.modelsphere.plugins.integrity")
include(":org.modelsphere.plugins.validation")
include(":org.modelsphere.plugins.java.genmeta")
include(":org.modelsphere.plugins.layout.cluster.rectanglepacker")
include(":org.modelsphere.plugins.report")
include(":org.modelsphere.plugins:repository_functions")
include(":org.modelsphere.plugins.sql_shell_activator")

// ─────────────────────────────────────────────────────────────
// Exemples / démos
// ─────────────────────────────────────────────────────────────
include(":org.modelsphere.plugins.helloworld_v3.1")
include(":org.modelsphere.plugins.helloworld_v3.2")