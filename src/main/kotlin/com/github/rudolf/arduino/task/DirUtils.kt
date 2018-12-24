package com.github.rudolf.arduino.task

import org.apache.ant.compress.taskdefs.UnXZ
import org.apache.commons.io.FilenameUtils
import org.gradle.api.internal.AbstractTask
import java.io.File
import java.nio.file.Paths

fun AbstractTask.extract(src: File, dest: File) {
    when (FilenameUtils.getExtension(src.path)) {
        "xz" -> unxz(src, dest)
        "zip" -> unzip(src, dest)
        "bz2" -> unbz2(src, dest)
        else -> throw UnsupportedOperationException("Unsupported archive: ${src}")
    }
}

fun AbstractTask.findDirWithContent(dest: File): File {
    val childs: Array<out File>? = dest.listFiles()
    if (childs != null && childs.isNotEmpty() && childs.size == 1 && childs.first().isDirectory) {
        return findDirWithContent(childs.first())
    }
    return dest
}

fun AbstractTask.unzip(src: File, dest: File) {
    project.copy {
        from(project.zipTree(src))
        into(dest)
    }
}
fun AbstractTask.unbz2(src: File, dest: File) {
    project.copy {
        from(project.tarTree(project.resources.bzip2(src.toURI())))
        into(dest)
    }
}

fun AbstractTask.unxz(src: File, dest: File) {
    val tmp = Paths.get(getTemporaryDir().path, "tmp.tar.xz").toFile()

    val x = UnXZ()
    x.setSrc(src)
    x.setDest(tmp)
    x.execute()

    project.copy {
        from(project.tarTree(tmp))
        into(dest)
    }
    project.delete(tmp)
}

fun String.preparePath() =
        this
                .replace(":","_")
                .replace(",", "_")
                .replace("=","_")
