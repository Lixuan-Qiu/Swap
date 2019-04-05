# deploy script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into the
# web deploy directory.

# This is the resource folder where maven expects to find our files

TARGETFOLDER=./local
# TARGETFOLDER=../localServer/src/main/resources
# This is the folder that we used with the Spark.staticFileLocation command


# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER


# there are many more steps to be done.  For now, we will just copy an HTML file
cp index.html $TARGETFOLDER
cp item.html $TARGETFOLDER
# step 2: update our npm dependencies
npm update

# step 3: copy javascript files
cp node_modules/jquery/dist/jquery.min.js $TARGETFOLDER
cp node_modules/handlebars/dist/handlebars.min.js $TARGETFOLDER
cp node_modules/bootstrap/dist/js/bootstrap.min.js $TARGETFOLDER
cp node_modules/bootstrap/dist/css/bootstrap.min.css $TARGETFOLDER
cp -R node_modules/bootstrap/dist/fonts $TARGETFOLDER

# step 4: compile TypeScript files
node_modules/typescript/bin/tsc app.ts --strict --outFile $TARGETFOLDER/app.js

# step 5: copy css files
cp app.css $TARGETFOLDER

# step 6: compile handlebars templates to the deploy folder
node_modules/handlebars/bin/handlebars hb/itemList.hb >> $TARGETFOLDER/templates.js
node_modules/handlebars/bin/handlebars hb/itemInfo.hb >> $TARGETFOLDER/templates.js
node_modules/.bin/http-server $TARGETFOLDER -c-1


