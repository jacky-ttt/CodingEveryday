#include <opencv2/core/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <string>
using namespace cv;
using namespace std;

cv::Mat ResizeKeepAspectRatio(const cv::Mat &input, const cv::Size &dstSize, const cv::Scalar &bgcolor)
{
	cv::Mat output;

	double h1 = dstSize.width * (input.rows / (double)input.cols);
	double w2 = dstSize.height * (input.cols / (double)input.rows);
	if (h1 <= dstSize.height) {
		cv::resize(input, output, cv::Size(dstSize.width, h1));
	}
	else {
		cv::resize(input, output, cv::Size(w2, dstSize.height));
	}

	int top = (dstSize.height - output.rows) / 2;
	int down = (dstSize.height - output.rows + 1) / 2;
	int left = (dstSize.width - output.cols) / 2;
	int right = (dstSize.width - output.cols + 1) / 2;

	cv::copyMakeBorder(output, output, top, down, left, right, cv::BORDER_CONSTANT, bgcolor);

	return output;
}

int main(int argc, char** argv)
{
	string imageName("dog.jpg"); // by default
	if (argc > 1)
	{
		imageName = argv[1];
	}
	Mat image;
	image = imread(imageName.c_str(), IMREAD_COLOR); // Read the file
	if (image.empty())                      // Check for invalid input
	{
		cout << "Could not open or find the image" << std::endl;
		return -1;
	}


	// full screen
	namedWindow("Display window", CV_WINDOW_NORMAL); // Create a window for display.
	cvSetWindowProperty("Display window", CV_WND_PROP_FULLSCREEN, CV_WINDOW_FULLSCREEN);

	Mat resizedImage = ResizeKeepAspectRatio(image, Size(1920, 1080), Scalar(0, 0, 0));
	imshow("Display window", resizedImage);
	waitKey(0); // Wait for a keystroke in the window


	return 0;
}
