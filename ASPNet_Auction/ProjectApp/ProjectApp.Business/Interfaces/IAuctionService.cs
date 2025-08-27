using ProjectApp.Data.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Business.Interfaces
{
    public interface IAuctionService
    {
        Task CreateAuctionAsync(Auction auction);
        Task EditAuctionDescriptionAsync(Guid auctionId, string newDescription);
        Task<IEnumerable<Auction>> GetOngoingAuctionsAsync();
        Task<Auction> GetAuctionDetailsAsync(Guid auctionId);
        Task PlaceBidAsync(Guid auctionId, string userId, decimal amount);
        Task<IEnumerable<Auction>> GetUserBidsAsync(string userId);
        Task<IEnumerable<Auction>> GetWonAuctionsAsync(string userId);

    }
}
