using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging.Abstractions;
using ProjectApp.Business.Interfaces;
using ProjectApp.Data.Models;
using ProjectApp.Shared.ViewModels;
using System.Collections.Generic;
using System.Security.Claims;
using X.PagedList.Extensions;

namespace ProjectApp.Controllers
{
    public class AuctionController : Controller
    {
        private readonly ILogger<AuctionController> _logger;
        private IAuctionService _auctionService;
        private readonly UserManager<IdentityUser> _userManager;

        public AuctionController(ILogger<AuctionController> logger, IAuctionService auctionService, UserManager<IdentityUser> userManager)
        {
            _logger = logger;
            _auctionService = auctionService;
            _userManager = userManager;
        }

        public async Task<IActionResult> OngoingAuctions(int? page)
        {
            // Set the current page and page size
            int pageNumber = page ?? 1; // Default to page 1 if no page is specified
            int pageSize = 10; // Number of items per page

            // Initialize the main view model
            var model = new AuctionVM
            {
                lstAuctionViewModel = new List<AuctionVM>()
            };

            // Fetch ongoing auctions
            var result = await _auctionService.GetOngoingAuctionsAsync();

            // Populate the list if results are not null and contain items
            if (result != null && result.Any())
            {
                foreach (var item in result)
                {
                    model.lstAuctionViewModel.Add(new AuctionVM
                    {
                        Id = item.Id,
                        Name = item.Name,
                        EndTime = item.EndTime,
                        StartingPrice = item.StartingPrice,
                        Description = item.Description,
                        OwnerId= item.OwnerId
                    });
                }
            }

            // Apply pagination to the auction list
            var pagedList = model.lstAuctionViewModel.ToPagedList(pageNumber, pageSize);

            // Return the view with the paginated model
            return View(pagedList);
        }


        public async Task<IActionResult> UserAuctions(int? page)
        {
            // Set the current page and page size
            
            int pageNumber = page ?? 1; // Default to page 1 if no page is specified
            int pageSize = 10; // Number of items per page

            // Initialize the main view model
            var model = new AuctionVM
            {
                lstAuctionViewModel = new List<AuctionVM>()
            };

            var userId = User.Claims.FirstOrDefault(c => c.Type == "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")?.Value;

            // Fetch ongoing auctions for the user
            var result = await _auctionService.GetUserBidsAsync(userId);

            // Convert the result to a list of AuctionVM
            var auctionList = result?.Select(item => new AuctionVM
            {
                Id = item.Id,
                Name = item.Name,
                EndTime = item.EndTime,
                StartingPrice = item.StartingPrice,
                Description = item.Description,
                OwnerId = item.OwnerId
            }).ToList();

            // Apply pagination
            var pagedAuctions = auctionList.ToPagedList(pageNumber, pageSize);

            // Return the view with the paginated model
            return View(pagedAuctions);
        }


        public async Task<IActionResult> WonAuctions(int? page)
        {
            // Set the current page and page size
            int pageNumber = page ?? 1; // Default to page 1 if no page is specified
            int pageSize = 10; // Number of items per page

            // Initialize the main view model
            var model = new AuctionVM
            {
                lstAuctionViewModel = new List<AuctionVM>()
            };

            var userId = User.Claims.FirstOrDefault(c => c.Type == "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")?.Value;

            // Fetch won auctions
            var result = await _auctionService.GetWonAuctionsAsync(userId);

            // Convert the result to a list of AuctionVM
            var auctionList = result?.Select(item => new AuctionVM
            {
                Id = item.Id,
                Name = item.Name,
                EndTime = item.EndTime,
                StartingPrice = item.StartingPrice,
                Description = item.Description,
                OwnerId = item.OwnerId
            }).ToList();

            // Apply pagination using PagedList
            var pagedAuctions = auctionList.ToPagedList(pageNumber, pageSize);

            // Return the view with the paginated model
            return View(pagedAuctions);
        }

        public async Task<IActionResult> AuctionDetails(string aid)
        {
            try
            {
                ViewBag.maxValue = 0;
                ViewBag.minValue = 0;
                Auction auctionDetail = await _auctionService.GetAuctionDetailsAsync(new Guid(aid));
                if (auctionDetail != null)
                {
                    if (auctionDetail.Bids != null && auctionDetail.Bids.Count() > 0)
                    {
                        foreach (var item in auctionDetail.Bids)
                        {
                            var user = await _userManager.FindByIdAsync(item.UserId);
                            item.UserName = user != null ? user.UserName : string.Empty;
                        }
                        ViewBag.maxValue = auctionDetail.Bids.Max(i => i.BidAmount);
                        ViewBag.minValue = auctionDetail.Bids.Min(i => i.BidAmount);
                    }
                }

               
                if (auctionDetail == null)
                {
                    return NotFound(new { Message = "Auction not found or has already ended." });
                }

                return View(auctionDetail);
            }
            catch (Exception ex)
            {
                // Log the exception (you can inject ILogger and log here)
                return StatusCode(500, new { Message = "An error occurred while retrieving the auction details.", Error = ex.Message });
            }
        }


        public async Task<IActionResult> WonAuctionDetails(string aid)
        {
            try
            {
                ViewBag.maxValue = 0;
                ViewBag.minValue = 0;
                Auction auctionDetail = await _auctionService.GetAuctionDetailsAsync(new Guid(aid));
                if (auctionDetail != null)
                {
                    auctionDetail.AuctionType = "WonAuctionDetails";
                    if (auctionDetail.Bids != null && auctionDetail.Bids.Count() > 0)
                    {
                        foreach (var item in auctionDetail.Bids)
                        {
                            var user = await _userManager.FindByIdAsync(item.UserId);
                            item.UserName = user != null ? user.UserName : string.Empty;
                        }
                        ViewBag.maxValue = auctionDetail.Bids.Max(i => i.BidAmount);
                        ViewBag.minValue = auctionDetail.Bids.Min(i => i.BidAmount);
                    }
                }


                //if (auctionDetail == null)
                //{
                //    return NotFound(new { Message = "Auction not found or has already ended." });
                //}
               
                return View(auctionDetail);
            }
            catch (Exception ex)
            {
                // Log the exception (you can inject ILogger and log here)
                return StatusCode(500, new { Message = "An error occurred while retrieving the auction details.", Error = ex.Message });
            }
        }

        public async Task<IActionResult> UserAuctionDetails(string aid)
        {
            try
            {
                ViewBag.maxValue = 0;
                ViewBag.minValue = 0;
                Auction auctionDetail = await _auctionService.GetAuctionDetailsAsync(new Guid(aid));
                if (auctionDetail != null)
                {
                    if (auctionDetail.Bids != null && auctionDetail.Bids.Count() > 0)
                    {
                        foreach (var item in auctionDetail.Bids)
                        {
                            var user = await _userManager.FindByIdAsync(item.UserId);
                            item.UserName = user != null ? user.UserName : string.Empty;
                        }
                        ViewBag.maxValue = auctionDetail.Bids.Max(i => i.BidAmount);
                        ViewBag.minValue = auctionDetail.Bids.Min(i => i.BidAmount);
                    }
                }


                if (auctionDetail == null)
                {
                    return NotFound(new { Message = "Auction not found or has already ended." });
                }
                auctionDetail.AuctionType = "UserAuctionDetails";
                return View(auctionDetail);
            }
            catch (Exception ex)
            {
                // Log the exception (you can inject ILogger and log here)
                return StatusCode(500, new { Message = "An error occurred while retrieving the auction details.", Error = ex.Message });
            }
        }



        [HttpPost]
        public async Task<IActionResult> PlaceBid([FromBody] BidPostModel model)
        {
            try
            {
                // Simulating the current user's ID (Replace with your actual user identity mechanism)
                string userId = User.FindFirstValue(ClaimTypes.NameIdentifier); // Ensure `using System.Security.Claims;`

                // Validate user ID
                if (string.IsNullOrEmpty(userId))
                {
                    return Json(new { success = false, message = "User not authenticated." });
                }

                // Call the service method to place a bid
                await _auctionService.PlaceBidAsync(new Guid(model.AuctionId), userId, model.BidAmount);

                TempData["ToastrMessage"] = "Bid placed successfully.";
                TempData["ToastrType"] = "success"; // Toastr type

                return Json(new { success = true, message = "Bid placed successfully." });

               


            }
            catch (Exception ex)
            {
                // Log the exception if necessary
                _logger.LogError(ex, "Error while placing bid for auction ID {AuctionId}", model.AuctionId);

                return Json(new { success = false, message = ex.Message });
            }
        }

        [HttpPost]
        public async Task<IActionResult> CreateAuction([FromBody] AuctionPostModel model)
        {
            if (!ModelState.IsValid)
            {
                return Json(new { success = false, message = "Invalid auction data. Please review your input." });
            }

            try
            {
                // Map ViewModel to Domain Model
                var auction = new Auction
                {
                    Name = model.Name,
                    Description = model.Description,
                    StartingPrice = model.StartingPrice,
                    EndTime = Convert.ToDateTime(model.EndTime),
                    OwnerId = User.FindFirstValue(ClaimTypes.NameIdentifier) // Set owner ID from logged-in user
                };

                // Call the service method to save the auction
                await _auctionService.CreateAuctionAsync(auction);

                TempData["ToastrMessage"] = "Auction created successfully.";
                TempData["ToastrType"] = "success"; // Toastr type
                return Json(new { success = true, message = "Auction created successfully." });
            }
            catch (Exception ex)
            {
                // Log the exception if necessary
                _logger.LogError(ex, "Error while creating auction.");

                return Json(new { success = false, message = "An error occurred while creating the auction. Please try again later." });
            }
        }


        [HttpPost]
        public async Task<IActionResult> Edit(Guid auctionId, string description)
        {
            if (string.IsNullOrWhiteSpace(description))
            {
                return Json(new { success = false, errors = new { description = "Description is required." } });
            }

            TempData["ToastrMessage"] = "Auction updated successfully.";
            TempData["ToastrType"] = "success"; // Toastr type

            // Perform the update logic here
            await _auctionService.EditAuctionDescriptionAsync(auctionId, description);

            return Json(new { success = true });
        }



    }
}
