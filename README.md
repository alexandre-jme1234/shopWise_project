### WORKFLOW GITLAB

Workflow se déploie au puhs sur la main, lsq une Pull Request est ouverte ou mises à jour en déploiement à la demande (workflow_dispatch).
Deux jobs s'enchainent, l'un teste le code avant de le build dans un second jobs.
Chaque feature sera développé dans une branche à part (feature/NOM_FEATURE) chaque commit suivra l'historique du developpement de la feature.
Lorsqu'une merge est fait sur la main, on pull, push ce qui lance le workflow.


### Java Spring template project

This project is based on a GitLab [Project Template](https://docs.gitlab.com/ee/gitlab-basics/create-project.html).

Improvements can be proposed in the [original project](https://gitlab.com/gitlab-org/project-templates/spring).

### CI/CD with Auto DevOps

This template is compatible with [Auto DevOps](https://docs.gitlab.com/ee/topics/autodevops/).

If Auto DevOps is not already enabled for this project, you can [turn it on](https://docs.gitlab.com/ee/topics/autodevops/#enabling-auto-devops) in the project settings.


