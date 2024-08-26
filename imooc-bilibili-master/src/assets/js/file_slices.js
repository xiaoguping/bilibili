import SparkMD5 from 'spark-md5';

function fileSlices(file, chunkSize) {
	let md5Obj = {
		files: [],
		md5: ''
	};
	return new Promise((resolve, reject) => {
		let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,
			chunks = Math.ceil(file.size / chunkSize),
			currentChunk = 0,
			spark = new SparkMD5.ArrayBuffer(),
			fileReader = new FileReader();

		fileReader.onload = function(e) {
			console.log('read chunk nr', currentChunk + 1, 'of', chunks);
			console.log(e.target.result);
			let _file = new File([e.target.result], currentChunk+1 + '.' + file.name.split('.')[1], {type: file.type});
			md5Obj.files.push(_file);
			spark.append(e.target.result);
			currentChunk++;

			if (currentChunk < chunks) {
				loadNext();
			} else {
				md5Obj.md5 = spark.end();
				md5Obj.total = chunks;
				md5Obj.totalSize = file.size;
				console.log('finished loading');
				resolve(md5Obj);
			}
		};

		fileReader.onerror = function(e) {
			reject(e);
		};

		function loadNext() {
			var start = currentChunk * chunkSize,
				end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

			fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
		}

		loadNext();
	})
}
export { fileSlices };