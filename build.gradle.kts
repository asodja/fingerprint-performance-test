import java.util.*

plugins {
    id("base")
}

abstract class GenerateFiles : DefaultTask() {
    @get:Input
    abstract val numOfFiles: Property<Int>
    @get:Input
    abstract val singeFileSizeInBytes: Property<Int>
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun run() {
        val rand = SplittableRandom()
        for (i in 0 until numOfFiles.get()) {
            val bytes = ByteArray(singeFileSizeInBytes.get())
            rand.nextBytes(bytes)
            outputDir.file("file-$i.txt").get().asFile.writeBytes(bytes)
        }
    }
}

val generateFiles = tasks.register<GenerateFiles>("generateFiles") {
    numOfFiles = 10_000
    singeFileSizeInBytes = 500_000
    outputDir = layout.buildDirectory.dir("generated-files")
}

val doNotTrack: String? by project
val deleteCopied = tasks.register<Delete>("deleteCopied") {
    mustRunAfter(generateFiles)
    delete(layout.buildDirectory.dir("copied-files"))
}

tasks.register<Copy>("copy") {
    dependsOn(deleteCopied)
    from(generateFiles)
    into(layout.buildDirectory.dir("copied-files"))
    if (doNotTrack?.trim() == "true") {
        doNotTrackState("")
    }
}