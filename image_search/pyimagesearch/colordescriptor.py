import numpy as np
import cv2

class ColorDescriptor:
	def __init__(self, bins):
		# 直方图的采样数目
		self.bins = bins

	def describe(self, image):
		# hsv转换
		image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
		features = []

		# 切个尺寸
		(h, w) = image.shape[:2]
		(cX, cY) = (int(w * 0.5), int(h * 0.5))

		# 图片分割区域
		segments = [(0, cX, 0, cY), (cX, w, 0, cY), (cX, w, cY, h),
			(0, cX, cY, h)]

		# 填充椭圆形
		(axesX, axesY) = (int(w * 0.75) / 2, int(h * 0.75) / 2)
		ellipMask = np.zeros(image.shape[:2], dtype = "uint8")
		# 绘制
		cv2.ellipse(ellipMask, (cX, cY), (axesX, axesY), 0, 0, 360, 255, -1)

		for (startX, endX, startY, endY) in segments:

			cornerMask = np.zeros(image.shape[:2], dtype = "uint8")
			cv2.rectangle(cornerMask, (startX, startY), (endX, endY), 255, -1)
			cornerMask = cv2.subtract(cornerMask, ellipMask)


			hist = self.histogram(image, cornerMask)
			features.extend(hist)

		# 提取中间的椭圆形
		hist = self.histogram(image, ellipMask)
		features.extend(hist)

		return features

	def histogram(self, image, mask):
		hist = cv2.calcHist([image], [0, 1, 2], mask, self.bins,
			[0, 180, 0, 256, 0, 256])
		hist = cv2.normalize(hist).flatten()

		return hist