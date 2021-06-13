var about_video_id = 0;
var results_video_id = 0;
var about_videos = ["./Assets/Videos/App-Connection.mov", "./Assets/Videos/Custom-LED.mov", "./Assets/Videos/Custom-Text.mov"];
var results_videos = ["./Assets/Videos/Wrench-Outfit-Pose.mp4", "./Assets/Videos/Wrench-Outfit-Gun.mp4"];

function aboutVideoChanger(element) {
  var video = document.getElementById('about-video');
  if (element.classList.contains("next-button")) {
    about_video_id += 1;
  }
  else {
    about_video_id -= 1;
  }
  if (about_video_id > about_videos.length - 1) {
    about_video_id = 0;
  }
  if (about_video_id < 0) {
    about_video_id = about_videos.length - 1;
  }
  video.setAttribute("src", about_videos[about_video_id]);
}

function resultsVideoChanger(element) {
  var video = document.getElementById('results-video');
  if (element.classList.contains("next-button")) {
    results_video_id += 1;
  }
  else {
    results_video_id -= 1;
  }
  if (results_video_id > results_videos.length - 1) {
    results_video_id = 0;
  }
  if (results_video_id < 0) {
    results_video_id = results_videos.length - 1;
  }
  video.setAttribute("src", results_videos[results_video_id]);
}
