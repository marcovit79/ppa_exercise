How would you design the development and release process of a web application that makes it possible to release the "master" branch at any time? How would you manage incrementally developing multiple features in parallel while frequently releasing to production?

In this use case I can use "git-flow". 
Explained https://nvie.com/posts/a-successful-git-branching-model/ or
https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow

I try to explain it briefly:
1) Master branch receive only tested code.
2) The development happen on a different branch: "develop"
3) Usually to develop a new feature you open a new branch from develop 
4) Small fix and small improvement can be committed directly to develop.
5) When you need to release you open a new "release" branch. You test the code 
   on this branch and mx correction here and on develop. When all is fine you 
   can merge "release" to master and to develop.
6) If you find a Bug in production you can open an hotfix branch from master,
   fix, test and merge back to master and develop.


For continuous deployment of master branch you can simply use a jenkins job or 
gitlab pipeline or any other CI/CD tool.

