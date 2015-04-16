


# Introduction #
These steps cover how to download the current Zeddic common library via Subclipse, and create a new Android project that refers to them.




# Pre-Android Setup #
**Impotant**: Before you start following these steps you _must_ install the Android SDK and Eclipse plugin. You can find details at http://developer.android.com/sdk/installing.html

These instructions will not work if you do not have the SDK installed.





# Setting up Subclipse #

In Eclipse click: Help --> Install New Software

Under the "work with field", add:

`http://subclipse.tigris.org/update_1.6.x`

Install 'subclipse' from the list of available software. The installation will take a few minutes, after which you'll need to restart Eclipse.

Add the Subsclipe view to your perspective. Click Window --> Show View --> Other --> SVN --> SVN Repositories





# Downloading and Importing the Common Project #
Once the new view has been added, hit the +SVN button and enter the url:

`http://zeddic-android.googlecode.com/svn/trunk/`

![http://zeddic-android.googlecode.com/svn-history/r16/wiki/svn-setup-location.png](http://zeddic-android.googlecode.com/svn-history/r16/wiki/svn-setup-location.png)

If you're running OSX, you might run into an error here because you're missing some of the neccessary Subversion binaries. You can download them at http://www.open.collab.net/downloads/community/

After a few seconds the repository should be visible. Right click on the trunk and click "checkout". The project details should already be filled out for you, click: Finish.

![http://zeddic-android.googlecode.com/svn-history/r16/wiki/new-project-wizard.png](http://zeddic-android.googlecode.com/svn-history/r16/wiki/new-project-wizard.png)

The library should now be setup and ready to be used!





# Creating a Basic Android Project #

Click on "File" --> "New" --> "Android Project"

![http://zeddic-android.googlecode.com/svn-history/r16/wiki/new-android-project.png](http://zeddic-android.googlecode.com/svn-history/r16/wiki/new-android-project.png)

File out the details for your new project, including a version of the Android SDK you want to build against.

After creating your new project, enter the project properties page and go to the "Android" section.

Under Library, add a reference to Common.

![http://zeddic-android.googlecode.com/svn-history/r16/wiki/common.png](http://zeddic-android.googlecode.com/svn-history/r16/wiki/common.png)

Your project is now setup to use the Common Library!