import tkinter
from PIL import Image, ImageTk


def fit_center(pil_image):
    root = tkinter.Tk()
    w, h = root.winfo_screenwidth(), root.winfo_screenheight()
    root.overrideredirect(1)
    root.geometry("%dx%d+0+0" % (w, h))
    root.focus_set()
    root.bind("<Escape>", lambda e: (e.widget.withdraw(), e.widget.quit()))
    canvas = tkinter.Canvas(root, width=w, height=h, highlightthickness=0)
    canvas.pack()
    canvas.configure(background='black')

    img_width, img_height = pil_image.size
    ratio = min(w / img_width, h / img_height)
    img_width = int(img_width * ratio)
    img_height = int(img_height * ratio)
    pil_image = pil_image.resize((img_width, img_height), Image.ANTIALIAS)

    image = ImageTk.PhotoImage(pil_image)
    imagesprite = canvas.create_image(w / 2, h / 2, image=image)
    root.mainloop()


pilImage = Image.open("background.jpg")
# pilImage = Image.open("puppy.jpg")
fit_center(pilImage)
