echo "Started Build Documentation";
echo "-------------------------------------------------------------"
echo "-------------------------------------------------------------"

cd ..

yarn build-doc
cp docs/index.html docs/404.html
echo -e "---\npermalink: /404.html\n---" >> docs/404.html
git add docs
git commit -m "Updated at `date '+%H:%M:%S %A %d-%B, %Y'`"
git push --all



