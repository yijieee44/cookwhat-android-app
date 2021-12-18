# CookWhat
CookWhat is a cooking mobile application with the goal of providing a fine experience to cater everyone that cooks and ease their troubles in preparing meals. Users are allowed to search recipe based on the ingredients and share recipe in the app.

## Workflow
To contribute, please follow the workflow below. **Only** push directly to the `main` branch if you know exactly what you're doing. Evaluate the impact of change before doing so.

1. Make sure your local main is up-to-date.

    ```
    git checkout main
    git pull origin main
    ```
2. Create a new local branch from your main branch to work on your changes.
    ```
    git checkout -b <my-feature-branch>
    ```
3. Make changes on your new branch. Add. Commit. Repeat.
    ```
    git add .
    git commit -m "A meaningful commit message"
    ```
4. When you are ready to push your branch, make sure it's up-to-date with the remote main first.
    ```
    git checkout main
    git pull origin main
    git checkout <my-feature-branch>
    git merge main
    ```
5. Resolve conflicts if there's any.

6. Make sure there's no more conflict and you're now ready to push your changes to the remote repository.
    ```
    git push origin <my-feature-branch>
    ```
7. Open a merge request on github to merge your branch to main.
    https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request


## Coding Styles
Kindly adhere to the naming convention and folder organization:
https://guides.codepath.com/android/Organizing-your-Source-Files