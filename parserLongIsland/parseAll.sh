for file in `ls $1 | grep '^task'`
do
	if [ -d "$1/$file" ]; then
		sh parse.sh "$1/$file"
	fi
done
