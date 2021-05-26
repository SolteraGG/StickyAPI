import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class DownloadTask extends DefaultTask {
    @Input
    String from

    @OutputFile
    File destination

    @TaskAction
    void download() {
        ant.get(src: from, dest: destination)
    }
}