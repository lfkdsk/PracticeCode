from pyimagesearch.colordescriptor import ColorDescriptor
import argparse
import glob
import cv2

# 设置命令行程序
ap = argparse.ArgumentParser()
ap.add_argument("-d", "--dataset", required = True,
	help = "Path to the directory that contains the images to be indexed")
ap.add_argument("-i", "--index", required = True,
	help = "Path to where the computed index will be stored")
args = vars(ap.parse_args())

# 初始化色彩描述 ／饱和度／色相／明度
cd = ColorDescriptor((8, 12, 3))

# index
output = open(args["index"], "w")

# 递归搜索
for imagePath in glob.glob(args["dataset"] + "/*.png"):
	
	# 用文件名当id来用
	imageID = imagePath[imagePath.rfind("/") + 1:]
	image = cv2.imread(imagePath)

	# 描述
	features = cd.describe(image)

	
	features = [str(f) for f in features]
	output.write("%s,%s\n" % (imageID, ",".join(features)))


output.close()