import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class GdProjectDiscoveryActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val service = GdProjectService.getInstance(project)
        service.discoverProject()
    }
}
