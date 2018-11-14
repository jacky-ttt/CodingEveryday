#include <algorithm>
#include <cstdlib>
#include <functional>
#include <iostream>
#include <memory>
#include <string>
#include <thread>
#include <vector>
#include <windows.h>
#include <math.h>


#include <vtkSmartPointer.h>
#include <vtkImageResize.h>
#include <vtkImageSincInterpolator.h>

#include <vtkImageReader2Factory.h>
#include <vtkImageReader2.h>
#include <vtkCamera.h>
#include <vtkImageActor.h>
#include <vtkImageCanvasSource2D.h>
#include <vtkImageData.h>
#include <vtkImageMapper3D.h>
#include <vtkRenderer.h>
#include <vtkRenderWindow.h>
#include <vtkRenderWindowInteractor.h>
#include <vtkInteractorStyleImage.h>
#include <vtkNamedColors.h>



using namespace std;

const double pi = std::acos(-1);

void displayImage(const char* fileAbsPath) {
	vtkSmartPointer<vtkImageData> imageData;

	int newSize[2] = { 1920, 1080 };
	int windowFunction = 0;


	// Read the image
	vtkSmartPointer<vtkImageReader2Factory> readerFactory =
		vtkSmartPointer<vtkImageReader2Factory>::New();
	vtkImageReader2 *reader = readerFactory->CreateImageReader2(fileAbsPath);
	reader->SetFileName(fileAbsPath);
	reader->Update();

	imageData = reader->GetOutput();
	reader->Delete();


	vtkSmartPointer<vtkImageSincInterpolator> interpolator =
		vtkSmartPointer<vtkImageSincInterpolator>::New();
	interpolator->UseWindowParameterOn();
	if (windowFunction >= 0 && windowFunction <= 10)
	{
		interpolator->SetWindowFunction(windowFunction);
	}

	vtkSmartPointer<vtkImageResize> resize =
		vtkSmartPointer<vtkImageResize>::New();
	resize->SetInputData(imageData);
	resize->SetInterpolator(interpolator);
	resize->SetOutputDimensions(newSize[0], newSize[1], 1);
	resize->InterpolateOn();

	if (windowFunction < 0)
	{
		resize->InterpolateOff();
		std::cout << "Using nearest neighbor interpolation" << std::endl;;
	}
	else
	{
		std::cout << "Using window function : "
			<< interpolator->GetWindowFunctionAsString() << std::endl;;
	}

	// Create an image actor to display the image
	vtkSmartPointer<vtkImageActor> imageActor =
		vtkSmartPointer<vtkImageActor>::New();
	imageActor->GetMapper()->SetInputConnection(resize->GetOutputPort());
	imageActor->InterpolateOff();

	// Setup renderer
	vtkSmartPointer<vtkNamedColors> colors =
		vtkSmartPointer<vtkNamedColors>::New();

	vtkSmartPointer<vtkRenderer> renderer =
		vtkSmartPointer<vtkRenderer>::New();
	renderer->AddActor(imageActor);
	// set background as black
	renderer->SetBackground(0, 0, 0);
	renderer->ResetCamera();
	// calculate how far the camera should be placed
	double a = renderer->GetActiveCamera()->GetViewAngle();
	double b = renderer->GetActiveCamera()->GetDistance();
	double c = ((3840 / 2) / tan(a / 180 * pi / 2));
	renderer->GetActiveCamera()->Dolly(b / c);

	renderer->ResetCameraClippingRange();

	// Setup render window
	vtkSmartPointer<vtkRenderWindow> renderWindow =
		vtkSmartPointer<vtkRenderWindow>::New();
	renderWindow->AddRenderer(renderer);
	renderWindow->SetWindowName("FullScreen");
	renderWindow->SetFullScreen(true);

	// Setup render window interactor
	vtkSmartPointer<vtkRenderWindowInteractor> renderWindowInteractor =
		vtkSmartPointer<vtkRenderWindowInteractor>::New();
	vtkSmartPointer<vtkInteractorStyleImage> style =
		vtkSmartPointer<vtkInteractorStyleImage>::New();

	renderWindowInteractor->SetInteractorStyle(style);

	// Render and start interaction
	renderWindowInteractor->SetRenderWindow(renderWindow);
	renderWindowInteractor->Initialize();

	// remove all interaction
	renderWindowInteractor->RemoveAllObservers();
	// hide cursor
	renderWindowInteractor->HideCursor();

	renderWindowInteractor->Start();
}


string getExePath() {
	char buffer[MAX_PATH];
	GetModuleFileName(NULL, buffer, MAX_PATH);
	string::size_type pos = string(buffer).find_last_of("\\/");
	return string(buffer).substr(0, pos);
}


int main(int, char *[])
{
	string exePath = getExePath();
	string imageName = exePath + "/../../background.jpg";
	displayImage(imageName.c_str());
	return EXIT_SUCCESS;
}
