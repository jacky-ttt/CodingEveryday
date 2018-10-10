#include <opencv2/core/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <string>
using namespace cv;
using namespace std;



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


	imshow("Display window", image);
	waitKey(0); // Wait for a keystroke in the window


	return 0;
}
