using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using ProjectApp.Business.Interfaces;
using ProjectApp.Business.Services;
using ProjectApp.Data.DbContext;

var builder = WebApplication.CreateBuilder(args);

// Configure logging
builder.Logging.ClearProviders(); // Clear default logging providers
builder.Logging.AddConsole(); // Add console logging
builder.Logging.AddDebug(); // Optional: Add debug logging

// Register services
builder.Services.AddControllersWithViews(); // This adds services for controllers and views

// Register Identity services
builder.Services.AddIdentity<IdentityUser, IdentityRole>()
    .AddEntityFrameworkStores<CustomIdentityDbContext>()
    .AddDefaultTokenProviders();

// Add services to the container.

// Register DbContext for Identity using MySQL
builder.Services.AddDbContext<CustomIdentityDbContext>(options =>
    options.UseMySql(
        builder.Configuration.GetConnectionString("IdentityConnection"),
        new MySqlServerVersion(new Version(8, 0, 32)) // Replace with your MySQL version
    ));

// Register DbContext for AuctionAppDbContext using MySQL
builder.Services.AddDbContext<AuctionAppDbContext>(options =>
    options.UseMySql(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        new MySqlServerVersion(new Version(8, 0, 32)) // Replace with your MySQL version
    ));

// Register other services
builder.Services.AddScoped<IAuctionService, AuctionService>();
builder.Services.AddTransient<IEmailSender, EmailSender>();
builder.Services.AddHttpContextAccessor();

// Authentication setup
builder.Services.AddAuthentication("Cookies")
    .AddCookie(options =>
    {
        options.LoginPath = "/Account/Login"; // Path to your login page
        options.LogoutPath = "/Account/Logout"; // Path to your logout action
    });

// Add Authorization services
builder.Services.AddAuthorization(); // Add this line to fix the issue
var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts(); // The default HSTS value is 30 days. You may want to change this for production scenarios.
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthorization();

// Map controller routes
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
