using Microsoft.EntityFrameworkCore;
using ProjectApp.Business.Interfaces;
using ProjectApp.Data.DbContext;
using ProjectApp.Data.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Business.Services
{
    public class AuctionService : IAuctionService
    {
        private readonly AuctionAppDbContext _context;

        public AuctionService(AuctionAppDbContext context)
        {
            _context = context;
        }

        public async Task CreateAuctionAsync(Auction auction)
        {
            try
            {
                _context.tblAuctions.Add(auction);
                await _context.SaveChangesAsync();
            }
            catch (Exception ex)
            {
            }
        }

        public async Task EditAuctionDescriptionAsync(Guid auctionId, string newDescription)
        {
            var auction = await _context.tblAuctions.FindAsync(auctionId);
            if (auction == null) throw new Exception("Auction not found.");
            auction.Description = newDescription;
            await _context.SaveChangesAsync();
        }



        public async Task<IEnumerable<Auction>> GetOngoingAuctionsAsync()
        {
            return await _context.tblAuctions
                .Where(a => a.EndTime > DateTime.Now)
                .OrderBy(a => a.EndTime)
                .ToListAsync();
        }

        public async Task<Auction> GetAuctionDetailsAsync(Guid auctionId)
        {
            return await _context.tblAuctions
                .Include(a => a.Bids)
                .FirstOrDefaultAsync(a => a.Id == auctionId);
        }

        public async Task PlaceBidAsync(Guid auctionId, string userId, decimal amount)
        {
            try
            {
                var auction = await _context.tblAuctions
               .Include(a => a.Bids)
               .FirstOrDefaultAsync(a => a.Id == auctionId);

                if (auction == null) throw new Exception("Auction not found.");
                //if (auction.OwnerId == userId) throw new Exception("Cannot bid on your own auction.");
                //if (auction.Bids.Any() && amount <= auction.Bids.Max(b => b.BidAmount)) throw new Exception("Bid must be higher than current highest bid.");
                //if (amount <= auction.StartingPrice) throw new Exception("Bid must be higher than the starting price.");

                var bid = new Bid
                {
                    AuctionId = auctionId,
                    UserId = userId,
                    BidAmount = amount,
                    BidDateTime = DateTime.Now
                };

                _context.tblBids.Add(bid);
                await _context.SaveChangesAsync();
            }
            catch (Exception ex)
            {

            }

        }

        public async Task<IEnumerable<Auction>> GetUserBidsAsync(string userId)
        {
            return await _context.tblAuctions
                .Where(a => a.Bids.Any(b => b.UserId == userId))
                .ToListAsync();
        }

        public async Task<IEnumerable<Auction>> GetWonAuctionsAsync(string userId)
        {
            return await _context.tblAuctions
                .Where(a => a.EndTime <= DateTime.Now && a.Bids.Any(b => b.UserId == userId && b.BidAmount == a.Bids.Max(bid => bid.BidAmount)))
                .ToListAsync();
        }


    }


}
