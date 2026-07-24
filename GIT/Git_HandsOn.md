# Git Hands-on Exercises Solutions

## Hands-on 1: Git Configuration, Editor Setup, and Basic Commands

**Step 1: Setup your machine with Git Configuration**
```bash
# Check Git version
git --version

# Set global user configuration
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Verify configuration
git config --list
```

**Step 2: Integrate notepad++ as a default editor**
```bash
# First, ensure notepad++ path (e.g., C:\Program Files\Notepad++) is added to your Windows Environment Variables (Path).

# Create an alias for notepad++ (optional, but helpful in bash)
alias npp='notepad++.exe'

# Configure Git to use Notepad++ as the default editor
git config --global core.editor "'C:/Program Files/Notepad++/notepad++.exe' -multiInst -notabbar -nosession -noPlugin"

# Verify the default editor setting
git config --global -e
```

**Step 3: Add a file to source code repository**
```bash
# Initialize a new Git repository
mkdir GitDemo
cd GitDemo
git init

# Verify initialization (shows hidden .git folder)
ls -a

# Create and add content to welcome.txt
echo "Welcome to Git!" > welcome.txt

# Check status
git status

# Add the file to the staging area
git add welcome.txt

# Commit the file (this will open Notepad++ to enter the commit message if -m is omitted)
git commit -m "Initial commit: Added welcome.txt"

# Push to a remote repository (Assuming remote is already added as 'origin')
git pull origin master
git push -u origin master
```

---

## Hands-on 2: Ignoring Unwanted Files

```bash
# Create a log folder and a .log file
mkdir log
echo "log data" > log/error.log
echo "app trace" > app.log

# Create and update .gitignore
echo "*.log" > .gitignore
echo "log/" >> .gitignore

# Verify git status to ensure .log files and log folder are ignored
git status

# Commit the .gitignore file
git add .gitignore
git commit -m "Add .gitignore to ignore log files and directories"
```

---

## Hands-on 3: Branching and Merging

```bash
# Create a new branch
git branch GitNewBranch

# List all branches (current branch has a * next to it)
git branch -a

# Switch to the new branch
git checkout GitNewBranch
# (or git switch GitNewBranch)

# Add some files and commit
echo "Feature 1" > feature.txt
git add feature.txt
git commit -m "Add feature.txt in GitNewBranch"
git status

# Switch back to master
git checkout master

# List CLI differences between master and branch
git diff master..GitNewBranch

# (Optional) List visual differences if P4Merge is configured
git difftool master..GitNewBranch

# Merge the branch to the trunk (master)
git merge GitNewBranch

# Observe the logging
git log --oneline --graph --decorate

# Delete the branch after merging
git branch -d GitNewBranch
git status
```

---

## Hands-on 4: Resolving Merge Conflicts

```bash
# Verify master is in a clean state
git status

# Create a branch and add a file
git checkout -b GitWork
echo "<message>Hello from GitWork</message>" > hello.xml
git add hello.xml
git commit -m "Add hello.xml in GitWork branch"

# Switch to master
git checkout master

# Add the same file with different content to cause a conflict
echo "<message>Hello from Master</message>" > hello.xml
git add hello.xml
git commit -m "Add hello.xml in master branch"

# Observe the log
git log --oneline --graph --decorate --all

# Check differences
git diff GitWork

# Merge the branch to master (This will cause a CONFLICT)
git merge GitWork

# Observe the git markup inside hello.xml
cat hello.xml

# Resolve the conflict using a merge tool (e.g., P4Merge)
git mergetool

# Commit the resolved changes
git commit -m "Resolve merge conflict in hello.xml"

# Observe git status (Git usually creates a .orig backup file during mergetool)
git status

# Add the backup file extension to .gitignore
echo "*.orig" >> .gitignore
git add .gitignore
git commit -m "Ignore mergetool backup files"

# Delete the merged branch
git branch -d GitWork

# Observe the final log
git log --oneline --graph --decorate
```

---

## Hands-on 5: Clean Up and Push

```bash
# Verify master is in clean state
git status

# List out all available branches
git branch -a

# Pull the remote git repository to the master to ensure it's up to date
git pull origin master

# Push the local commits to the remote repository
git push origin master

# Observe the changes on the GitLab/GitHub remote repository interface.
```
