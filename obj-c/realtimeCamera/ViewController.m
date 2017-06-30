#import "ViewController.h"
#import <AVFoundation/AVFoundation.h>

@interface ViewController ()<AVCaptureVideoDataOutputSampleBufferDelegate>

@property (nonatomic, strong) AVCaptureVideoPreviewLayer  *preLayer;
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupCaptureSession];
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer fromConnection:(AVCaptureConnection *)connection
{
    // Sample Buffer ( CMSampleBufferRef ) To Image ( UIImage )
    UIImage *image = [self imageFromSampleBuffer:sampleBuffer];
    NSLog(@"output,mdata:%@",image);
}

// Start Camera Session with perview layout
- (void)setupCaptureSession
{
    NSError *error = nil;
    
    // Init session
    AVCaptureSession *session = [[AVCaptureSession alloc] init];
    
    //Camera
    session.sessionPreset = AVCaptureSessionPresetMedium;
    
    // Find a suitable AVCaptureDevice
    
    AVCaptureDevice *device;
    for(AVCaptureDevice *dev in [AVCaptureDevice devicesWithMediaType:AVMediaTypeVideo])
    {
        // set camera position ( front / back )
        if([dev position]==AVCaptureDevicePositionBack) //AVCaptureDevicePositionFront <-- Back Camera
        {
            device=dev;
            break;
        }
    }
    //  Init Camera Input
    AVCaptureDeviceInput *input = [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
    if (!input) { /* Handling the error appropriately */}
    [session addInput:input];
    
    // Init Video Data Output ( Get By -> captureOutput() )
    AVCaptureVideoDataOutput *output = [[AVCaptureVideoDataOutput alloc] init];
    [session addOutput:output];
    
    // set output queue ( cannot use main )
    dispatch_queue_t queue = dispatch_queue_create("myQueue", NULL);
    [output setSampleBufferDelegate:self queue:queue];
    
    // Specify the pixel format
    output.videoSettings = [NSDictionary dictionaryWithObjectsAndKeys:
                            [NSNumber numberWithInt:kCVPixelFormatType_32BGRA], kCVPixelBufferPixelFormatTypeKey,
                            [NSNumber numberWithInt: 640], (id)kCVPixelBufferWidthKey,
                            [NSNumber numberWithInt: 480], (id)kCVPixelBufferHeightKey,
                            nil];
    //  Perview Layer
    self.preLayer = [AVCaptureVideoPreviewLayer layerWithSession: session];
    self.preLayer.frame = self.view.frame;
    self.preLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    [self.view.layer addSublayer:self.preLayer];
    
    // Start Session
    [session startRunning];
    
}

// Buffer To UIImage
- (UIImage *) imageFromSampleBuffer:(CMSampleBufferRef) sampleBuffer
{
    // Get a CMSampleBuffer's Core Video image buffer for the media data
    CVImageBufferRef imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
    // Lock the base address of the pixel buffer
    CVPixelBufferLockBaseAddress(imageBuffer, 0);
    
    // Get the number of bytes per row for the pixel buffer
    void *baseAddress = CVPixelBufferGetBaseAddress(imageBuffer);
    
    // Get the number of bytes per row for the pixel buffer
    size_t bytesPerRow = CVPixelBufferGetBytesPerRow(imageBuffer);
    // Get the pixel buffer width and height
    size_t width = CVPixelBufferGetWidth(imageBuffer);
    size_t height = CVPixelBufferGetHeight(imageBuffer);
    
    // Create a device-dependent RGB color space
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    
    // Create a bitmap graphics context with the sample buffer data
    CGContextRef context = CGBitmapContextCreate(baseAddress, width, height, 8,
                                                 bytesPerRow, colorSpace, kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedFirst);
    // Create a Quartz image from the pixel data in the bitmap graphics context
    CGImageRef quartzImage = CGBitmapContextCreateImage(context);
    // Unlock the pixel buffer
    CVPixelBufferUnlockBaseAddress(imageBuffer,0);
    
    // Free up the context and color space
    CGContextRelease(context);
    CGColorSpaceRelease(colorSpace);
    
    // Create an image object from the Quartz image
    //UIImage *image = [UIImage imageWithCGImage:quartzImage];
    UIImage *image = [UIImage imageWithCGImage:quartzImage scale:1.0f orientation:UIImageOrientationRight];
    
    // Release the Quartz image
    CGImageRelease(quartzImage);
    
    return (image);
}

@end
