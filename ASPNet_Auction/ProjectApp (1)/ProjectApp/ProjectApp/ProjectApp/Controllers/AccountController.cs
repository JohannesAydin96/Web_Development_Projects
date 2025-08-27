using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using ProjectApp.Models;
using ProjectApp.Shared.ViewModels;
using System.Diagnostics;

namespace ProjectApp.Controllers
{
    public class AccountController : Controller
    {
        private readonly ILogger<HomeController> _logger;
        private readonly UserManager<IdentityUser> _userManager;
        private readonly SignInManager<IdentityUser> _signInManager;
        private readonly Business.Interfaces.IEmailSender _emailSender;

        // Constructor with dependency injection for UserManager, SignInManager, EmailSender, and Logger
        public AccountController(UserManager<IdentityUser> userManager, SignInManager<IdentityUser> signInManager, Business.Interfaces.IEmailSender emailSender, ILogger<HomeController> logger)
        {
            _logger = logger;
            _userManager = userManager;
            _signInManager = signInManager;
            _emailSender = emailSender;
        }

        // Index action, returns the default view
        public IActionResult Index()
        {
            return View();
        }

        // Registration action
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Register(RegisterVM model)
        {
            if (!ModelState.IsValid)
            {
                if (Request.Headers["X-Requested-With"] == "XMLHttpRequest")
                {
                    return Json(new { success = false, errors = ModelState.Values.SelectMany(v => v.Errors).Select(e => e.ErrorMessage) });
                }
                return View(model);
            }

            // Check for duplicate username
            var existingUsernameUser = await _userManager.FindByNameAsync(model.Username);
            if (existingUsernameUser != null)
            {
                ModelState.AddModelError(string.Empty, "Username is already in use.");
                return Json(new { success = false, errors = new[] { "Username is already in use." } });

            }

            // Check for duplicate email
            var existingEmailUser = await _userManager.FindByEmailAsync(model.Email);
            if (existingEmailUser != null)
            {
                ModelState.AddModelError(string.Empty, "Email is already in use.");
                return Json(new { success = false, errors = new[] { "Email is already in use." } });

            }

            // Create a new user
            var user = new IdentityUser
            {
                UserName = model.Username,
                Email = model.Email
            };

            // Create the user and hash the password
            var result = await _userManager.CreateAsync(user, model.Password!);
            if (result.Succeeded)
            {
                // Generate email confirmation token
                var token = await _userManager.GenerateEmailConfirmationTokenAsync(user);

                // Create a confirmation link
                var confirmationLink = Url.Action(
                    nameof(ConfirmEmail),
                    "Home",
                    new { userId = user.Id, token },
                    Request.Scheme);

                // Send email
                await _emailSender.SendEmailAsync(
                    model.Email,
                    "Confirm your email",
                    $"Please confirm your account by clicking <a href='{confirmationLink}'>here</a>.");


                return Json(new { success = true, message = "Registration successful! Please check your email to confirm your account." });


            }

            // Handle registration errors
            foreach (var error in result.Errors)
            {
                ModelState.AddModelError(string.Empty, error.Description);
            }

            if (Request.Headers["X-Requested-With"] == "XMLHttpRequest")
            {
                return Json(new { success = false, errors = ModelState.Values.SelectMany(v => v.Errors).Select(e => e.ErrorMessage) });
            }
            return View(model);
        }


        // Confirmation of email after user clicks the link
        [HttpGet]
        public async Task<IActionResult> ConfirmEmail(string userId, string token)
        {
            if (userId == null || token == null)
            {
                return BadRequest("Invalid confirmation link.");
            }

            var user = await _userManager.FindByIdAsync(userId);
            if (user == null)

            {
                return NotFound("User not found.");
            }

            var result = await _userManager.ConfirmEmailAsync(user, token);
            if (result.Succeeded)
            {
                return RedirectToAction("Index", "Home");
            }

            return BadRequest("Email confirmation failed.");
        }

        // Login action


        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Login(LoginVM model)
        {
            if (!ModelState.IsValid)
            {
                ViewBag.ToastrMessage = "Invalid login credentials.";
                ViewBag.ToastrType = "error"; // Toastr type
                return RedirectToAction("Index", "Home");
            }

            // Fetch the user by username or email
            var user = await _userManager.FindByNameAsync(model.UserName);
            if (user == null)
            {
                ModelState.AddModelError("", "Invalid login attempt.");
                TempData["ToastrMessage"] = "Invalid login attempt.";
                TempData["ToastrType"] = "error"; // Toastr type
            }
            // Attempt sign-in
            var result = await _signInManager.PasswordSignInAsync(model.UserName, model.Password, isPersistent: false, lockoutOnFailure: false);
            if (result.Succeeded)
            {
                return RedirectToAction("Index", "Home");
            }
            else
            {
                TempData["ToastrMessage"] = "Invalid login credentials.";
                TempData["ToastrType"] = "error"; // Toastr type
            }

            return RedirectToAction("Index", "Home");
            //// Handle failed login
            //ModelState.AddModelError("", "Invalid login attempt.");
            //return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync();
            return RedirectToAction("Index", "Home"); // Redirect to the home page or login page
        }

        // Example view for ongoing auctions
        public IActionResult OngoingAuction()
        {
            return View();
        }

        // Privacy policy view
        public IActionResult Privacy()
        {
            return View();
        }

        // Error handling view
        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
